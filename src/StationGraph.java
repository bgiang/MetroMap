import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;




public class StationGraph {
	HashMap<String,HashSet<String>> adjmap;
	public StationGraph(TreeMap<String, StationInfo> stationlist){
	     adjmap = new HashMap<String, HashSet<String>>();
	     Set<String> stationset=stationlist.keySet();
	     Iterator<String> stationiter=stationset.iterator();
	     while(stationiter.hasNext()){
	    	 String tempstat=stationiter.next();
	    	 adjmap.put(tempstat, new HashSet<String>());
	     }
		
	}
	
	public void addNeighbor(String vertex,String edge){
		adjmap.get(vertex).add(edge);
	}
	
	public HashSet<String> getneighbors(String vertex){
		return adjmap.get(vertex);
	}
}
