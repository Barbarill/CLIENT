import client.MainTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * La classe ControllerScena2 gestisce gli eventi e la logica della scena Scena2.fxml,
 * inclusa la visualizzazione del nome della tabella selezionata e la navigazione
 * tra le varie scene dell'applicazione.
 */
public class ControllerScena2 {

    /** Etichetta per visualizzare il nome della tabella selezionata. */
    @FXML private Label tableNameLabel;

    /** Etichetta per visualizzare messaggi informativi per l'utente. */
    @FXML private Label messageLabel;

    /** Bottone per tornare alla scena precedente. */
    @FXML private Button backButton;

    /** Riferimento all'istanza di MainTest per la connessione e la comunicazione con il server. */
    private MainTest mainTest;

    /** Riferimento allo Stage principale per il controllo delle scene dell'applicazione. */
    private Stage primaryStage;

    /**
     * Imposta l'istanza di MainTest da utilizzare per le operazioni con il server.
     * @param mainTest l'istanza di MainTest da impostare
     */
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    /**
     * Imposta lo Stage principale per il controllo delle scene.
     * @param primaryStage lo Stage principale dell'applicazione
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Imposta e visualizza il nome della tabella selezionata nell'etichetta.
     * @param tableName il nome della tabella da visualizzare
     */
    public void setTableName(String tableName) {
        tableNameLabel.setText("Tabella selezionata: " + tableName);
    }

    /**
     * Gestisce l'evento di click sul bottone "Indietro", caricando la scena precedente.
     */
    @FXML
    private void onBackButtonClick() {
        loadScene("/Scena1.fxml");
    }

    /**
     * Carica una scena specificata e imposta i controller necessari per il contesto.
     * Questo metodo consente di passare a diverse scene dell'applicazione.
     *
     * @param fxmlFile il file FXML della scena da caricare
     */
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            // Ottiene il controller della nuova scena e imposta MainTest e Stage.
            Object controller = loader.getController();
            if (controller instanceof ControllerScena1) {
                ControllerScena1 mainController = (ControllerScena1) controller;
                mainController.setMainTest(mainTest);
                mainController.setPrimaryStage(primaryStage);
            } else if (controller instanceof ControllerScena3) {
                ControllerScena3 controllerScena3 = (ControllerScena3) controller;
                controllerScena3.setMainTest(mainTest);
                controllerScena3.setPrimaryStage(primaryStage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il caricamento del dendrogramma da un file e passa alla scena Scena3.fxml.
     */
    @FXML
    private void onLoadDendrogramFromFile() {
        loadScene("/Scena3.fxml");
    }

    /**
     * Avvia l'apprendimento del dendrogramma dal database e torna alla scena Scena1.fxml.
     */
    @FXML
    private void onLearnDendrogramFromDB() {
        loadScene("/Scena1.fxml");
    }
}