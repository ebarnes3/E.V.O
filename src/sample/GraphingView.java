package sample;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class GraphingView {
    @FXML
    private HBox graphingbox;

    private MainApp mainApp;

    public GraphingView(){

    }
    @FXML
    private void initialize(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Graphing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Graphing().setVisible(true);
        });
    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }
}
