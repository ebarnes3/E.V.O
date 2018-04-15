package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PracticeView {


    private MainApp mainApp;

    @FXML
    private HBox practicebox;

    public PracticeView(){

    }
    @FXML
    private void initialize(){
//        Stage stage;
//        PracticeGUI gui = new PracticeGUI();
//        gui.start(stage);
    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }
}
