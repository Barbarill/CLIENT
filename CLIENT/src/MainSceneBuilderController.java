import client.MainTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la scena principale, responsabile di gestire l'invio del nome della tabella al server e
 * il caricamento della scena successiva.
 */
public class MainSceneBuilderController {

    /** Campo di testo per l'inserimento del nome della tabella. */
    @FXML
    private TextField tableNameField;

    /** Etichetta per la visualizzazione dei messaggi all'utente. */
    @FXML
    private Label messageLabel;

    /** Riferimento all'istanza di MainTest per la gestione della comunicazione col server. */
    private MainTest mainTest;

    /** Riferimento allo Stage principale, usato per cambiare le scene. */
    private Stage primaryStage;

    /**
     * Imposta l'istanza di MainTest.
     * @param mainTest l'istanza di MainTest da utilizzare per la comunicazione col server.
     */
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    /**
     * Imposta lo Stage principale.
     * @param primaryStage lo Stage principale dell'applicazione.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gestisce il click sul pulsante di conferma per il nome della tabella.
     * Invia il nome della tabella al server e carica la scena successiva in caso di successo.
     */
    @FXML
    private void onTableNameConfirm() {
        String tableName = tableNameField.getText();

        if (tableName == null || tableName.trim().isEmpty()) {
            messageLabel.setText("Errore: Nome tabella non valido.");
            return;  // Esce dal metodo se il nome è vuoto
        }

        if (mainTest != null) {
            try {
                // Passa il nome della tabella al server senza passare dalla linea di comando
                sendTableNameToServer(tableName);

                messageLabel.setText("Nome tabella inviato correttamente!");

                // Carica la nuova scena (ad esempio Scena4)
                loadScene("/Scena4.fxml");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                messageLabel.setText("Errore durante l'invio del nome della tabella.");
            }
        } else {
            messageLabel.setText("Errore: mainTest non è stato inizializzato.");
        }
    }

    /**
     * Invia il nome della tabella al server tramite l'istanza di MainTest.
     * @param tableName il nome della tabella da inviare.
     * @throws IOException se si verifica un errore nella comunicazione con il server.
     * @throws ClassNotFoundException se si verifica un errore nella ricezione della risposta dal server.
     */
    private void sendTableNameToServer(String tableName) throws IOException, ClassNotFoundException {
        mainTest.setTableName(tableName); // Imposta il nome della tabella in MainTest
        mainTest.getOut().writeObject(0); // Codice per inviare il nome della tabella
        mainTest.getOut().writeObject(tableName); // Invia il nome della tabella
        String risposta = (String) mainTest.getIn().readObject();
        if (!"OK".equals(risposta)) {
            throw new IOException("Errore dal server: " + risposta);
        }
    }

    /**
     * Carica e visualizza una nuova scena specificata.
     * @param fxmlFile il percorso del file FXML da caricare.
     */
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.getScene().setRoot(root); // Cambia la scena
            } else {
                System.out.println("Errore: primaryStage è null");
            }

            // Ottiene il controller della scena caricata e passa le dipendenze
            Object controller = loader.getController();

            if (controller instanceof ControllerScena2) {
                ((ControllerScena2) controller).setMainTest(mainTest);
                ((ControllerScena2) controller).setPrimaryStage(primaryStage);
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
}
