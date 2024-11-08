import client.MainTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerScena2 {

    @FXML private Label tableNameLabel;
    @FXML private Label messageLabel;
    @FXML private Button backButton;

    private MainTest mainTest;  // Riferimento a MainTest
    private Stage primaryStage; // Riferimento allo Stage principale

    // Metodo per impostare l'istanza di MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    // Metodo per impostare lo Stage principale
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo per ricevere e visualizzare il nome della tabella nella seconda scena
    public void setTableName(String tableName) {
        tableNameLabel.setText("Tabella selezionata: " + tableName);
    }

    // Metodo per tornare alla scena precedente (MainScene.fxml)
    @FXML
    private void onBackButtonClick() {
        loadScene("/MainScene.fxml");
    }

    // Metodo per caricare e passare alla scena specificata
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            // Ottieni il controller della scena caricata
            Object controller = loader.getController();

            // Imposta `mainTest` e `primaryStage` a seconda del controller della scena
            if (controller instanceof MainSceneBuilderController) {
                ((MainSceneBuilderController) controller).setMainTest(mainTest);
                ((MainSceneBuilderController) controller).setPrimaryStage(primaryStage);
            } else if (controller instanceof ControllerScena3) {
                ((ControllerScena3) controller).setMainTest(mainTest);
                ((ControllerScena3) controller).setPrimaryStage(primaryStage);
            } else if (controller instanceof ControllerScena4) {
                ((ControllerScena4) controller).setMainTest(mainTest);
                ((ControllerScena4) controller).setPrimaryStage(primaryStage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Metodo per inviare il comando di caricamento del dendrogramma dal server
    @FXML
    private void onLoadDendrogramFromFile() {
        // Passa alla Scena 3 per chiedere il nome del file
        loadScene("/Scena3.fxml");
    }


    // Metodo per avviare l'apprendimento del dendrogramma dal database
    @FXML
    private void onLearnDendrogramFromDB() {
        // Passa alla Scena4 per l'inserimento della profondità e della distanza
        loadScene("/Scena4.fxml");
    }
}
