import client.MainTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            Object controller = loader.getController();
            if (controller instanceof MainSceneBuilderController) {
                MainSceneBuilderController mainController = (MainSceneBuilderController) controller;
                mainController.setMainTest(mainTest);
                mainController.setPrimaryStage(primaryStage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo per inviare il comando di caricamento del dendrogramma dal server
    @FXML
    private void onLoadDendrogramFromFile() {
        loadScene("/Scena3.fxml");
    }

    // Metodo per avviare l'apprendimento del dendrogramma dal database
    @FXML
    private void onLearnDendrogramFromDB() {
        loadScene("/MainScene.fxml");
    }
}
