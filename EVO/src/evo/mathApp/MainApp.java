package evo.mathApp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    
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

	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);




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
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error:");
	        }
	    }
	 
	 public Stage getPrimaryStage() {
	        return primaryStage;
	    }

	 
	public static void main(String[] args) {
		launch(args);
	}
}
