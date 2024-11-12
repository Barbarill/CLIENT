import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 * ControllerScena4 gestisce le operazioni nella scena 4 dell'applicazione JavaFX,
 * permettendo all'utente di caricare e salvare dendrogrammi, e di navigare tra le scene.
 */
public class ControllerScena4 {

    /** Campo di testo per inserire la profondità del dendrogramma. */
    @FXML private TextField depthField;

    /** Campo di testo per inserire la distanza tra cluster del dendrogramma. */
    @FXML private TextField distanceField;

    /** Campo di testo per inserire il nome del file in cui salvare il dendrogramma. */
    @FXML private TextField fileNameField;

    /** Etichetta per visualizzare i messaggi di errore o di stato all'utente. */
    @FXML private Label messageLabel;

    /** Area di testo per visualizzare il dendrogramma generato. */
    @FXML private TextArea dendrogramTextArea;

    /** Istanza della classe MainTest per gestire le operazioni client-server. */
    private MainTest mainTest;

    /** Finestra principale dell'applicazione. */
    private Stage primaryStage;

    /** Flag che indica se il dendrogramma è stato caricato con successo. */
    private boolean isConfirmed = false;

    /** Flag che indica se il dendrogramma è stato salvato correttamente. */
    private boolean isSaved = false;

    /**
     * Imposta l'istanza di MainTest per la comunicazione con il server.
     * @param mainTest l'istanza di MainTest da assegnare.
     */
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    /**
     * Imposta la finestra principale dell'applicazione.
     * @param primaryStage la finestra principale da assegnare.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gestisce il clic sul pulsante di conferma. Valida i campi di input e invia una richiesta al server
     * per caricare il dendrogramma con i parametri specificati.
     */
    @FXML
    private void onConfirmButtonClick() {
        // Controllo e parsing dei campi di input
        messageLabel.setText("");
        isConfirmed = false;

        try {
            int depth;
            int distance;

            // Validazione e parsing della profondità
            String depthText = depthField.getText().trim();
            if (!depthText.isEmpty()) {
                try {
                    depth = Integer.parseInt(depthText);
                    if (depth <= 0) {
                        messageLabel.setText("Errore: la profondità deve essere un numero positivo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    messageLabel.setText("Errore: la profondità deve essere un numero intero.");
                    return;
                }
            } else {
                messageLabel.setText("Errore: il campo della profondità è vuoto.");
                return;
            }

            // Validazione e parsing della distanza
            String distanceText = distanceField.getText().trim();
            if (!distanceText.isEmpty()) {
                try {
                    distance = Integer.parseInt(distanceText);
                    if (distance <= 0) {
                        messageLabel.setText("Errore: la distanza deve essere un numero positivo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    messageLabel.setText("Errore: la distanza deve essere un numero intero.");
                    return;
                }
            } else {
                messageLabel.setText("Errore: il campo della distanza è vuoto.");
                return;
            }

            // Invio dati al server
            mainTest.getOut().reset();
            mainTest.getOut().writeObject(1);
            mainTest.getOut().writeObject(mainTest.getTableName());
            mainTest.getOut().writeObject(depth);
            mainTest.getOut().writeObject(distance);

            // Ricezione della risposta dal server
            String risposta = (String) mainTest.getIn().readObject();
            if ("OK".equals(risposta)) {
                dendrogramTextArea.setText((String) mainTest.getIn().readObject());
                messageLabel.setText("Dendrogramma caricato correttamente.");
                isConfirmed = true;
            } else {
                messageLabel.setText("Errore dal server: " + risposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore nella comunicazione con il server.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il clic sul pulsante di salvataggio. Valida il campo nome file e invia una richiesta
     * al server per salvare il dendrogramma con il nome specificato.
     */
    @FXML
    private void onSaveButtonClick() {
        String fileName = fileNameField.getText().trim();

        if (fileName.isEmpty()) {
            messageLabel.setText("Errore: nome file non valido.");
            return;
        }

        try {
            mainTest.getOut().writeObject(fileName);  // Invio del nome del file al server
            String risposta = (String) mainTest.getIn().readObject();
            if ("OK".equals(risposta) || "Dendrogramma salvato correttamente.".equals(risposta)) {
                messageLabel.setText("Salvataggio riuscito.");
                isSaved = true;
            } else {
                messageLabel.setText("Errore dal server: " + risposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore durante il salvataggio del dendrogramma.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il clic sul pulsante di terminazione. Verifica che tutti i campi siano completati
     * e mostra un popup per confermare l'azione di terminazione o di continuazione.
     */
    @FXML
    private void onTerminaButtonClick() {
        String fileName = fileNameField.getText().trim();
        String depthText = depthField.getText().trim();
        String distanceText = distanceField.getText().trim();

        if (fileName.isEmpty() || depthText.isEmpty() || distanceText.isEmpty() || !isConfirmed || !isSaved) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Avviso");
            alert.setHeaderText(null);
            alert.setContentText("Terminare l'inserimento dei dati prima di procedere.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText(null);
            alert.setContentText("Vuoi fare un'altra operazione? (S/N)");

            ButtonType buttonTypeYes = new ButtonType("S");
            ButtonType buttonTypeNo = new ButtonType("N");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                loadScene("/Scena2.fxml");
            } else {
                closeConnection();
            }
        }
    }

    /**
     * Carica una nuova scena all'interno della finestra principale.
     * @param fxmlFile il percorso del file FXML della scena da caricare.
     */
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            Object controller = loader.getController();
            if (controller instanceof ControllerScena2) {
                ControllerScena2 controllerScena2 = (ControllerScena2) controller;
                controllerScena2.setMainTest(mainTest);
                controllerScena2.setPrimaryStage(primaryStage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Errore nel caricamento della scena.");
        }
    }

    /**
     * Chiude la connessione con il server e la finestra principale dell'applicazione.
     */
    private void closeConnection() {
        mainTest.closeConnection();
        primaryStage.close();
    }
}