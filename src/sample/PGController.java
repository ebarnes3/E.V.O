package evo.mathApp.view;

import evo.mathApp.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class PGController {

	@FXML
    private Button pgButton;
	
	@FXML
    private Pane pgPane;
	
	private MainApp mainApp;
	
	public PGController() {
		
	}
	
	 @FXML
	 private void initialize() {
		 pgPane.setStyle("-fx-background-color: #5e6672");
	 }
	 
	 public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;

	 }
}
