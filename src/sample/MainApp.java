package sample;

import java.io.IOException;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;
import sample.HomeViewController;
import sample.PGController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {
    private String[] items = {"home","calculator","graph","practice","shutdown"};

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
	            loader.setLocation(MainApp.class.getResource("RootLayout.fxml")); //goes to view package and gets HomeView
	            rootLayout = (BorderPane) loader.load();
	            rootLayout.getStylesheets().add(getClass().getResource("swag.css").toExternalForm());
	            rootLayout.setLeft(menu());
	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            //scene.getStylesheets().add("view/swag.css");
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
	            loader.setLocation(MainApp.class.getResource("HomeView.fxml"));
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
	 
	 private VBox menu() {

	        VBox vbox = new VBox();
	        vbox.setPrefWidth(60);

	        vbox.setStyle("-fx-background-color: #47476b");
                for(int i=0; i< items.length;i++){
                    vbox.getChildren().add(addItems(items[i]));
                }
	        return vbox;
	    }

	    private HBox addItems(String icon){
            Image image = new Image(MainApp.class.getResource(icon + ".png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(35);
            imageView.setFitWidth(50);
            imageView.setPreserveRatio(true);
            Button btn = new Button();
            if(icon.equals("home")){
                btn.setOnAction(e -> {
                    showHomeView();
                });
            }else if(icon.equals("calculator")){
                btn.setOnAction(e -> {
                    showHomeView();
                });

            }else if(icon.equals("graph")){
                btn.setOnAction(e ->{
                    getGraphingView();
                });
            }else if(icon.equals("shutdown")){
                btn.setOnAction(e ->{
                    System.exit(0);
                });
            }else if(icon.equals("practice")){
                btn.setOnAction(e ->{
                    getPractiveView();
                });
            }
            btn.setGraphic(imageView);
            btn.setPrefSize(45,50);
            Pane paneIndictor = new Pane();
            paneIndictor.setPrefSize(5,50);
            paneIndictor.setStyle("-fx-background-color: #8585e0");
            menuDecorator(btn,paneIndictor);
            HBox hbox = new HBox(paneIndictor,btn);

            return hbox;
        }
        public void menuDecorator(Button btn, Pane pane){
	        btn.setOnMouseEntered(e->{
                pane.setStyle("-fx-background-color: #ccb3ff");
                btn.setStyle("-fx-background-color: #ccb3ff");
            });
	        btn.setOnMouseExited(e -> {
	            pane.setStyle("-fx-background-color: #8585e0");
	            btn.setStyle("-fx-background-color: #8585e0");
            });
        }
	 
//	 private BorderPane getProblemGeneratorView() {
//	    System.out.println("getting problem generator view");
//		 try {
//
//			 FXMLLoader loader = new FXMLLoader();
//	         loader.setLocation(MainApp.class.getResource("ProblemGeneratorView.fxml"));
//	         //BorderPane pgView = (BorderPane) loader.load();
//	         problemGeneratorLayout = (BorderPane) loader.load();
//	         PGController controller = loader.getController();
//	         controller.setMainApp(this);
//	         //problemGeneratorLayout = pgView;
//
//		 } catch (IOException e) {
//	            e.printStackTrace();
//	            System.out.println("Error!");
//	        }
//		 return problemGeneratorLayout;
//	 }
	 private void getGraphingView(){
         try {
             // Load person overview.
             FXMLLoader loader = new FXMLLoader();
             loader.setLocation(MainApp.class.getResource("GraphingView.fxml"));
             BorderPane HomeView = (BorderPane) loader.load();

             // Set person overview into the center of root layout.
             rootLayout.setCenter(HomeView);


             // Give the controller access to the main app.
             GraphingView controller = loader.getController();
             controller.setMainApp(this);

         } catch (IOException e) {
             e.printStackTrace();
             System.out.println("Error!");
         }
     }
     private void getPractiveView(){
         try {
             // Load person overview.
             FXMLLoader loader = new FXMLLoader();
             loader.setLocation(MainApp.class.getResource("PracticeView.fxml"));
             BorderPane HomeView = (BorderPane) loader.load();

             // Set person overview into the center of root layout.
             rootLayout.setCenter(HomeView);


             // Give the controller access to the main app.
             PracticeView controller = loader.getController();
             controller.setMainApp(this);

         } catch (IOException e) {
             e.printStackTrace();
             System.out.println("Error!");
         }
     }

//	 private BorderPane addProblemGenerator() {
//		 BorderPane pg = new BorderPane();
//		 Label generatedProblemLabel = new Label("[Generated Problem Here]");
//		 pg.setTop(generatedProblemLabel);
//		 return pg;
//	 }
	 
	 public Stage getPrimaryStage() {
	        return primaryStage;
	    }
	 
	public static void main(String[] args) {
		launch(args);
	}
}
