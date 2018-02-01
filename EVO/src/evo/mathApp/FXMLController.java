package evo.mathApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FXMLController {
    private boolean displayKeyboard = false;

    public void initialize(){

    }
    @FXML
    protected void keyboardClicked(ActionEvent event) {
        if(displayKeyboard == false){
            displayKeyboard = true;

        }
        else if(displayKeyboard == true){
            displayKeyboard = false;
            

        }else{
            System.out.println("Boolean error with displayKeyboard");
        }
    }
}
