/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PracticeGUI extends Application {

    double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hello Drag And Drop");

        Group root = new Group();
        Scene scene = new Scene(root, 800, 800);
        scene.setFill(Color.LIGHTBLUE);

        Text source = new Text("2");
        source.setTranslateX(50);
        source.setTranslateY(100);
        source.setScaleX(5.0);
        source.setScaleY(5.0);
        source.setCursor(Cursor.HAND);

        source.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");
                orgSceneX = event.getSceneX();
                orgSceneY = event.getSceneY();
                orgTranslateX = ((Text) (event.getSource())).getTranslateX();
                orgTranslateY = ((Text) (event.getSource())).getTranslateY();
                System.out.println("getSceneX " + event.getSceneX() + " & " + " getSceneY " + event.getSceneY());

            }
        });

        source.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double offsetX = event.getSceneX() - orgSceneX;
                double offsetY = event.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;
                ((Text) (event.getSource())).setTranslateX(newTranslateX);
                ((Text) (event.getSource())).setTranslateY(newTranslateY);
                event.consume();
            }
        });

        final Text operater = new Text(175, 100, "+");
        operater.setScaleX(5.0);
        operater.setScaleY(5.0);

        final Text target = new Text(300, 100, "5");
        target.setScaleX(5.0);
        target.setScaleY(5.0);

        final Text operater2 = new Text(428, 100, "*");
        operater2.setScaleX(5.0);
        operater2.setScaleY(5.0);

        final Text operand = new Text(550, 100, "8");
        operand.setScaleX(5.0);
        operand.setScaleY(5.0);

//        source.setOnDragDetected(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
//                /* drag was detected, start drag-and-drop gesture*/
//                System.out.println("onDragDetected");
//
//                /* allow any transfer mode */
//                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
//
//                /* put a string on dragboard */
//                ClipboardContent content = new ClipboardContent();
//                content.putString("7");
//                db.setContent(content);
//            }
//        });
//
//        source.setOnDragDetected(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
//                /* drag was detected, start drag-and-drop gesture*/
//                System.out.println("onDragDetected");
//
//                /* allow any transfer mode */
//                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
//
//                /* put a string on dragboard */
//                ClipboardContent content = new ClipboardContent();
//                content.putString("7");
//                db.setContent(content);
//                //event.consume();
//            }
//        });

        target.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");

                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != target
                        && event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        target.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != target
                        && event.getDragboard().hasString()) {
                    target.setFill(Color.RED);
                }
                // event.consume();
            }
        });

        target.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                target.setFill(Color.BLACK);

                //event.consume();
            }
        });

//        target.setOnDragDropped(new EventHandler<DragEvent>() {
//            public void handle(DragEvent event) {
//                /* data dropped */
//                System.out.println("onDragDropped");
//                /* if there is a string data on dragboard, read it and use it */
//                Dragboard db = event.getDragboard();
//                boolean success = false;
//                if (db.hasString()) {
//                    target.setText(db.getString());
//
//                    success = true;
//                }
//                /* let the source know whether the string was successfully
//                 * transferred and used */
//                event.setDropCompleted(success);
//
//                //event.consume();
//            }
//        });
//
//        source.setOnDragDone(new EventHandler<DragEvent>() {
//            public void handle(DragEvent event) {
//                /* the drag-and-drop gesture ended */
//                System.out.println("onDragDone");
//                /* if the data was successfully moved, clear it */
//                if (event.getTransferMode() == TransferMode.MOVE) {
//                    source.setText("");
//                    source.setFill(Color.RED);
//                }
//
//                //event.consume();
//            }
//        });

        root.getChildren().add(source);
        root.getChildren().add(target);
        root.getChildren().add(operater);
        root.getChildren().add(operater2);
        root.getChildren().add(operand);
        stage.setScene(scene);
        stage.show();

    }
    PracticeGUI(){

    }

//    public static void main(String[] args) {
//        Application.launch(args);
//    }

}
