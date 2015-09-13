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
	public TreeMap<String,String> stationname=new TreeMap<String,String>();
	public TreeMap<String,String> stationline = new TreeMap<String,String>();
	public HashMap<String, String> stationmap=new HashMap<String,String>();
	
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
			            JsonNode tempele=childelement.next();
			            stationline.put(tempele.get("Code").textValue(), tempele.get("LineCode1").textValue());
			            stationname.put(tempele.get("Code").textValue(), tempele.get("Name").textValue());


			        }
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	       
	}
	public static void main(String[] args) {
		Stations test=new Stations("hi");
		Set<String> keyset=test.stationline.keySet();
		Iterator<String> iterator=keyset.iterator();
		ColorCode code=new ColorCode();
		while(iterator.hasNext()){
			String temp= iterator.next();
			System.out.println( temp + " " + 
			test.stationname.get(temp)+ " " 
			+ code.colorcode.get(test.stationline.get(temp)));
			
		}
		
	}
	
	
}
