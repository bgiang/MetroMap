import java.awt.Label;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.control.*;


@SuppressWarnings("restriction")
public class StationGui extends Application {

	//Replace API_KEY with your own
	private String apikey="API_KEY";
	@Override
	public void start(Stage primarystage) throws Exception {
		//Station setup 
		Stations station=new Stations(apikey);
		TreeMap<String,StationInfo> map=station.stationlist;
		Set<String> codelist=map.keySet();
		ColorCode colorcode=new ColorCode();
		// Grid Setup
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		// Add Element
		Text title = new Text("WMATA");
		grid.add(title, 0, 0, 1, 1);
		Text searchLabel = new Text("Pick Station Name:");
		grid.add(searchLabel, 0, 1);
		ComboBox<String> comboBox = new ComboBox<String>();
		Iterator<String> codeIter=codelist.iterator();
		String temp=codeIter.next();
		comboBox.getItems().add(temp+ " " + map.get(temp).name);
		comboBox.setValue(temp+ " " + map.get(temp).name);
		while(codeIter.hasNext()){
			temp=codeIter.next();
			comboBox.getItems().add(temp + " " + map.get(temp).name);
		}
	
		grid.add(comboBox, 1, 1);
		Button searchBtn = new Button();
		javafx.scene.control.Label streetlabel=new javafx.scene.control.Label();
		javafx.scene.control.Label streetlabel2=new javafx.scene.control.Label();
		javafx.scene.control.Label linelabel=new javafx.scene.control.Label();
		searchBtn.setText("Get Station Information");
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {

			
			public void handle(ActionEvent event) {
				String code=comboBox.getValue().substring(0,3);
				Address addr=map.get(code).address;
				streetlabel.setText(addr.street);
				streetlabel2.setText(addr.city + ", " + addr.state + " " + addr.zip);
				linelabel.setText(colorcode.colorcode.get(map.get(code).line)+ " Line");
			
			}
		});
		grid.add(searchBtn,0,2);
		grid.add(streetlabel, 0, 3,2,1);
		grid.add(streetlabel2, 0, 4,2,1);
		grid.add(linelabel, 0, 5,2,1);
		
		// Scene Setup
		Scene scene = new Scene(grid, 600, 200);
		primarystage.setTitle("Wmata Info");
		primarystage.setScene(scene);
		primarystage.show();

	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
