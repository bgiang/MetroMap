import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import com.fasterxml.jackson.databind.*;

public class Stations {
	private String apikey;
	TreeMap<String, StationInfo> stationlist = new TreeMap<String, StationInfo>();
	HashMap<String, LineInfo> linelist = new HashMap<String, LineInfo>();
	StationGraph statmap;
	public class LineInfo {
		String start;
		String end;
	}


	// Constructor (take in user apikey)
	public Stations(String keyin) {
		apikey = keyin;
		setStations();
		setLineInfo();
		statmap=new StationGraph(stationlist);
		setStationMap();
	}

	// Send in request to Wmata API and create map for station name and line
	// color for station code
	private void setStations() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readTree(new URL("https://api.wmata.com/Rail.svc/json/jStations?api_key=" + apikey));
			JsonNode child = node.get("Stations");
			Iterator<JsonNode> childelement = child.iterator();
			while (childelement.hasNext()) {
				StationInfo stationin = new StationInfo();
				JsonNode tempele = childelement.next();
				String codein = tempele.get("Code").textValue();
				stationin.line = tempele.get("LineCode1").textValue();
				stationin.name = tempele.get("Name").textValue();
				stationin.address = getAddress(tempele.get("Address"));
				stationlist.put(codein, stationin);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
//Get Line EndPoint
	private void setLineInfo(){
		ObjectMapper mapper=new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readTree(new URL("https://api.wmata.com/Rail.svc/json/jLines?api_key="+ apikey));
			JsonNode child=node.get("Lines");
			Iterator<JsonNode> childelement = child.iterator();
			while(childelement.hasNext()){
				LineInfo lineinfo=new LineInfo();
				JsonNode tempele=childelement.next();
				lineinfo.start=tempele.get("StartStationCode").textValue();
				lineinfo.end=tempele.get("EndStationCode").textValue();
				String linecodein=tempele.get("LineCode").textValue();
				linelist.put(linecodein, lineinfo);
	
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
//Create the Adjacency list for all the stations;
	private void setStationMap(){
		Set<String> lineset=linelist.keySet();
		Iterator<String> lineiter=lineset.iterator();
		while (lineiter.hasNext()){
			String curline=lineiter.next();
			String start=linelist.get(curline).start;
			String end=linelist.get(curline).end;
			ObjectMapper mapper=new ObjectMapper();
			JsonNode node;
			try {
				node = mapper.readTree(new URL("https://api.wmata.com/Rail.svc/json/jPath?FromStationCode="+start+"&ToStationCode="+ end 
						+"&api_key="+ apikey));
				JsonNode child=node.get("Path");
				Iterator<JsonNode> childelement = child.iterator();
				String prev="";
				String curr;
				while(childelement.hasNext()){
					JsonNode tempele=childelement.next();
					curr=tempele.get("StationCode").textValue();
					if(!prev.equals("")){
						statmap.addNeighbor(curr, prev);
						statmap.addNeighbor(prev, curr);
					}
					prev=curr;
					
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
		}
	}
	
	//Parse Address json node
	private Address getAddress(JsonNode addr) {
		Address addressin = new Address();
		addressin.street = addr.get("Street").textValue();
		addressin.city = addr.get("City").textValue();
		addressin.state = addr.get("State").textValue();
		addressin.zip = addr.get("Zip").textValue();

		return addressin;

	}
	
	public 	TreeMap<String, StationInfo> getstationlist(){
		return stationlist;
	}
	
	public 	HashMap<String, LineInfo> getLineList(){
		return linelist;
	}
	
	public StationGraph getmap(){
		return statmap;
	}
	public static void main(String[] args) {
		//Testing Graph
		Stations teststation=new Stations("API_KEY");
		Set<String> stationset=teststation.stationlist.keySet();
		Iterator<String> stationiter=stationset.iterator();
		while(stationiter.hasNext()){
			String temp=stationiter.next();
			Set<String> neighbors=teststation.statmap.getneighbors(temp);
			System.out.print(temp + " ");
			Iterator<String> neighiter=neighbors.iterator();
			while(neighiter.hasNext()){
				System.out.print(neighiter.next()+",");
			}
			System.out.println("");
		}
	}

}
