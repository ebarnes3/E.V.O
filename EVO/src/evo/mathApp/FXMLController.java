package evo.mathApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FXMLController {
    private boolean displayKeyboard = false;
    @FXML VBox keyboardVisability;

    public void initialize(){

    }
    @FXML
    protected void keyboardClicked(ActionEvent event) {
        //keyboardVisability.getChildren().add(lbl);

        if(displayKeyboard == false){
            displayKeyboard = true;
            final Button okButton = new Button("OK");
            okButton.setDefaultButton(true);
            okButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("OK Button Pressed!");
                }
            });

            //cancels field from accepting input
            final Button cancelButton = new Button("Cancel");
            cancelButton.setCancelButton(true);
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Canceled!");
                }
            });

            //disables keyboard from accepting input
            final CheckBox disabledCheckBox = new CheckBox("Disable");

            final HBox buttons = new HBox(5);
            buttons.getChildren().addAll(okButton, cancelButton, disabledCheckBox);
            buttons.setAlignment(Pos.CENTER);

            //keyboard body dimensions
            final VBox root = new VBox(5);
            root.setPadding(new Insets(10));
            Scene scene = new Scene(root);

            VirtualKeyboard vkb = new VirtualKeyboard();

            // just add a border to easily visualize the boundary of the keyboard:
            vkb.view().setStyle("-fx-border-color: grey; -fx-border-radius: 5;");
            vkb.view().disableProperty().bind(disabledCheckBox.selectedProperty());

            //if textarea is to be added
            //root.getChildren().addAll(textField, buttons, vkb.view());
            root.getChildren().addAll(buttons, vkb.view());
            keyboardVisability.getChildren().add(root);

        }
        else if(displayKeyboard == true){
            displayKeyboard = false;
            keyboardVisability.getChildren().clear();

        }else{
            System.out.println("Boolean error with displayKeyboard");
        }
    }
}
