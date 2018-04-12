package evo.mathApp;

import java.io.IOException;
import evo.mathApp.view.HomeViewController;
import evo.mathApp.view.PGController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private BorderPane problemGeneratorLayout;
    private BorderPane homeViewLayout;
    private Scene problemGeneratorScene;
    
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MathApp");

        initRootLayout();
        showHomeView();

	}
	
	 public void initRootLayout() {
	        try {
	            // Load root layout from fxml file.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml")); //goes to view package and gets HomeView 
	            rootLayout = (BorderPane) loader.load();
	            rootLayout.setLeft(addVBox());
	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            scene.getStylesheets().add("view/swag.css");
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public void showHomeView() {
	        try {
	            // Load person overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("view/HomeView.fxml"));
	            BorderPane HomeView = (BorderPane) loader.load();
	            
	            // Set person overview into the center of root layout.
	            rootLayout.setCenter(HomeView);
	           
	            
	            // Give the controller access to the main app.
	            HomeViewController controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error!");
	        }
	    }
	 
	 private VBox addVBox() {
	        
	        VBox vbox = new VBox();
	        //vbox.setPadding(new insets(10)); // Set all sides to 10
	        vbox.setSpacing(160);              // Gap between nodes
	        vbox.setPadding(new javafx.geometry.Insets(45,10,10,50)); //up,right,bottom,left
	        
	        Button button1 = new Button("Home");
	        button1.setOnAction(e -> rootLayout.setCenter(getHomeView()));
	        Button button2 = new Button("Common Input Functions");
	        Button button3 = new Button("Videos");
	        Button button4 = new Button("Problem Generator");
	        button4.setOnAction(e -> rootLayout.setCenter(getProblemGeneratorView()));
	        Button button5 = new Button("Exit");
	       
	        vbox.getChildren().addAll(button1,button2,button3, button4,button5);
	        
	        return vbox;
	    }
	 
	 private BorderPane getProblemGeneratorView() {
		 try {
			 
			 FXMLLoader loader = new FXMLLoader();
	         loader.setLocation(MainApp.class.getResource("view/ProblemGeneratorView.fxml"));
	         //BorderPane pgView = (BorderPane) loader.load();
	         problemGeneratorLayout = (BorderPane) loader.load();
	         PGController controller = loader.getController();
	         controller.setMainApp(this);
	         //problemGeneratorLayout = pgView;
	         
		 } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error!");
	        }
		 return problemGeneratorLayout;
	 }
	 
	 private BorderPane getHomeView() {
		 try {
			 
			 FXMLLoader loader = new FXMLLoader();
	         loader.setLocation(MainApp.class.getResource("view/HomeView.fxml"));
	         homeViewLayout = (BorderPane) loader.load();
	         HomeViewController controller = loader.getController();
	         controller.setMainApp(this);
	         
		 } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error!");
	        }
		 return homeViewLayout;
	 }
	 private BorderPane addProblemGenerator() {
		 BorderPane pg = new BorderPane();
		 Label generatedProblemLabel = new Label("[Generated Problem Here]");
		 pg.setTop(generatedProblemLabel);
		 return pg;
	 }
	 
	 public Stage getPrimaryStage() {
	        return primaryStage;
	    }
	 
	public static void main(String[] args) {
		launch(args);
	}
}
