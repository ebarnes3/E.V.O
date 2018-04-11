package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

import java.awt.*;

import sample.*;

//import evo.mathApp.MainApp;
//import evo.mathApp.VirtualKeyboard;
//import evo.mathApp.model.InToPost;
//import evo.mathApp.model.MathProblem;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class HomeViewController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label keyboardLabel;

    @FXML
    private Button keyboardButton;

    @FXML
    private Button goButton;

    @FXML
    private VBox keyboardVisability;

    @FXML
    private TextField inputTextField;

    @FXML
    private Label answerLabel;

    @FXML
    private Button viewStepsButton;

    @FXML
    private AnchorPane initialSolutionPane;

    @FXML
    private AnchorPane stepsAnchorPane;

    @FXML
    private VBox solutionStepsVBox, stepsVBox;

    @FXML
    private HBox topHBox;

    private boolean displayKeyboard = false;

    private static String input; //changed
    private static String output; //changed
    private static String solution; //changed
    private static ObservableList<String> steps;
    private static InToPost theTrans;

    // Reference to the main application.
    private MainApp mainApp;

    public HomeViewController() { //empty constructor

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        //solutionStepsVBox.setVisible(false); //hides solution and steps
        initialSolutionPane.setVisible(false);
        //stepsAnchorPane.setVisible(false);
        keyboardButton.setOnAction((event) -> {

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

                //if text area is to be added
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
        }); //end of keyboardButton action Handler


        goButton.setOnAction((event) -> {
            //resetSteps();
            if(initialSolutionPane.isVisible()) {
                steps.clear();
                for(int i=0; i< steps.size(); i++){
                    System.out.println(steps.get(i));
                }
                //if there is already a solution displayed
                //reset steps for new input
                stepsVBox.getChildren().clear();

                //steps.clear();
                //steps.removeAll();
                initialSolutionPane.setVisible(true);
                input = inputTextField.getText();

                InToPost theTrans = new InToPost(input);
                theTrans.signs();
                String truExpression = theTrans.handleNegativeNumber();
                output = theTrans.doTrans(truExpression);
                output = theTrans.editExpression(output);

                solution = theTrans.calculate(output,input);



                //InToPost theTrans = new InToPost(input);
//                theTrans = new InToPost(input);
//                output = theTrans.doTrans();
//                output = theTrans.editExpression(output);
//                solution = theTrans.calculate(output,input);
                answerLabel.setText(solution);
                answerLabel.setTextFill(Color.web("#0076a3"));
                steps = theTrans.getSteps();
            } else {
                //if there is no solution dislayed yet
                initialSolutionPane.setVisible(true);
                input = inputTextField.getText();

                InToPost theTrans = new InToPost(input);
                theTrans.signs();
                String truExpression = theTrans.handleNegativeNumber();
                output = theTrans.doTrans(truExpression);
                output = theTrans.editExpression(output);

                solution = theTrans.calculate(output,input);
                answerLabel.setText(solution);
                answerLabel.setTextFill(Color.web("#0076a3"));
                steps = theTrans.getSteps();
            }

    	  /*initialSolutionPane.setVisible(true);
    	  input = inputTextField.getText();
    	  InToPost theTrans = new InToPost(input);
    	  output = theTrans.doTrans();
      output = theTrans.editExpression(output);
      solution = theTrans.calculate(output,input);
      answerLabel.setText(solution);
      answerLabel.setTextFill(Color.web("#0076a3"));
      steps = theTrans.getSteps();*/

        });//end of goButton action handler

        viewStepsButton.setOnAction((event) -> {

            if(stepsVBox.isVisible()) {

                //steps.clear();
                //stepsVBox.getChildren().clear();


                for(int i=0; i<steps.size(); i++) {

                    AnchorPane pane = new AnchorPane();
                    pane.setPrefSize(603, 60);

                    Label label = new Label();
                    pane.setTopAnchor(label, 12.0);
                    pane.setLeftAnchor(label, 30.0);
                    pane.setRightAnchor(label, 70.0);
                    label.setText(steps.get(i));
                    label.setTextAlignment(TextAlignment.CENTER);
                    label.setTextFill(Color.web("#0076a3"));
                    label.setStyle("-fx-font:  22 Cambria;");
                    //label.setFont(Font.getFont("Cambria"));

                    Button button = new Button();
                    pane.setTopAnchor(button, 12.0);
                    pane.setRightAnchor(button, 20.0);
                    button.setText("Next Step");
                    button.setTextAlignment(TextAlignment.CENTER);

                    pane.getChildren().addAll(label, button);
                    stepsVBox.getChildren().addAll(pane);
                } //end of for loop


            }
            else {

                for(int i=0; i<steps.size(); i++) {

                    AnchorPane pane = new AnchorPane();
                    pane.setPrefSize(603, 60);

                    Label label = new Label();
                    pane.setTopAnchor(label, 12.0);
                    pane.setLeftAnchor(label, 30.0);
                    pane.setRightAnchor(label, 70.0);
                    label.setText(steps.get(i));
                    label.setTextAlignment(TextAlignment.CENTER);
                    label.setTextFill(Color.web("#0076a3"));
                    label.setStyle("-fx-font:  22 Cambria;");
                    //label.setFont(Font.getFont("Cambria"));

                    Button button = new Button();
                    pane.setTopAnchor(button, 12.0);
                    pane.setRightAnchor(button, 20.0);
                    button.setText("Next Step");
                    button.setTextAlignment(TextAlignment.CENTER);

                    pane.getChildren().addAll(label, button);
                    stepsVBox.getChildren().addAll(pane);

                }//end of for loop

            }
    	  /*for(int i=0; i<steps.size(); i++) {

    		  	AnchorPane pane = new AnchorPane();
    		  	pane.setPrefSize(603, 60);

    		  	Label label = new Label();
    		  	pane.setTopAnchor(label, 12.0);
    		  	pane.setLeftAnchor(label, 30.0);
    		  	pane.setRightAnchor(label, 70.0);
    		  	label.setText(steps.get(i));
    		  	label.setTextAlignment(TextAlignment.CENTER);
    		  	label.setTextFill(Color.web("#0076a3"));
    		  	label.setStyle("-fx-font:  22 Cambria;");
    		  	//label.setFont(Font.getFont("Cambria"));

    		  	Button button = new Button();
    		  	pane.setTopAnchor(button, 12.0);
    		  	pane.setRightAnchor(button, 20.0);
    		  	button.setText("Next Step");
    		  	button.setTextAlignment(TextAlignment.CENTER);

    		  	pane.getChildren().addAll(label, button);
    			stepsVBox.getChildren().addAll(pane);

    	  }*/
        });

    } //end of initializer
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }



}
