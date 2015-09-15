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
	TreeMap<String,StationInfo> stationlist=new TreeMap<String,StationInfo>();
	//Constructor (take in user apikey)
	public Stations(String keyin){
		apikey=keyin;	
		setStations();
	}
	
	//Send in request to Wmata API and create map for station name and line color for station code
	private void setStations(){
		  ObjectMapper mapper=new ObjectMapper();
	        JsonNode node;
			try {
				node = mapper.readTree(new URL("https://api.wmata.com/Rail.svc/json/jStations?api_key="+ apikey));
				 JsonNode child=node.get("Stations");
			        Iterator<JsonNode>  childelement=child.iterator();
			        while(childelement.hasNext()){
			        	StationInfo stationin=new StationInfo();
			            JsonNode tempele=childelement.next();
			            String codein=tempele.get("Code").textValue();
			            stationin.line=tempele.get("LineCode1").textValue();
			            stationin.name=tempele.get("Name").textValue();
			            stationin.address=getAddress(tempele.get("Address"));
			            stationlist.put(codein, stationin);
			        }
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	       
	}
	
	private Address getAddress(JsonNode addr){
		Address addressin=new Address();
		addressin.street=addr.get("Street").textValue();
		addressin.city=addr.get("City").textValue();
		addressin.state=addr.get("State").textValue();
		addressin.zip=addr.get("Zip").textValue();
		
		return addressin;
		
	}
	public static void main(String[] args) {
		Stations i=new Stations("628d226309af4a71b36b62788d9466e2");
		TreeMap<String,StationInfo>  testmap=i.stationlist;
		Set<String> codelist=testmap.keySet();
		Iterator<String> statIter=codelist.iterator();
		while(statIter.hasNext()){
			StationInfo temp=testmap.get(statIter.next());
			System.out.println(temp.address.street);
		}
		
	}
	
	
}
