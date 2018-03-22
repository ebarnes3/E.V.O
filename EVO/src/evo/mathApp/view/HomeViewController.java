package evo.mathApp.view;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import evo.mathApp.MainApp;
import evo.mathApp.VirtualKeyboard;
import evo.mathApp.model.InToPost;
import evo.mathApp.model.MathProblem;
import javafx.scene.paint.Color;

import static evo.mathApp.view.problemGenerator.parExpression;
import static evo.mathApp.view.problemGenerator.raNum;
import static evo.mathApp.view.problemGenerator.validExpression;

public class HomeViewController {

	@FXML
    private Label keyboardLabel;

	@FXML
    private Button keyboardButton;

	@FXML
    private Button goButton;

	@FXML
    private Button randomGenerator;

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
	private VBox solutionStepsVBox;

	@FXML
	private HBox topHBox;

	private boolean displayKeyboard = false;

	private String input;
	private String output;
	private String solution;
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
    stepsAnchorPane.setVisible(false);
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
      });//end of goButton action handler

      viewStepsButton.setOnAction((event) -> {
    	  stepsAnchorPane.setVisible(true);
      });

      randomGenerator.setOnAction((event) -> {
          String expression = "";
          int first = (int) Math.floor(Math.random()*15)+2;
          expression = expression + first;
          int numOfNums = (int) Math.floor(Math.random() * 5) + 5; // 6 - 10
          for(int i = 0; i < numOfNums - 1; i++) {
              expression = expression + raNum();
          }
          System.out.println("Raw: " + expression);
          expression = parExpression(expression);
          System.out.println("Par: " + expression);
          expression = validExpression(expression);
          System.out.println("Fin: " + expression);
          inputTextField.setText(expression);
      });

    } //end of initializer
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }



}
