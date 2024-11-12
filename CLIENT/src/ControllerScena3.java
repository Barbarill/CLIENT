import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * La classe ControllerScena3 gestisce gli eventi e la logica della scena Scena3.fxml,
 * inclusa la visualizzazione e il caricamento di un dendrogramma da file.
 */
public class ControllerScena3 {

    /** Campo di testo per inserire il nome del file del dendrogramma. */
    @FXML private TextField fileNameTextField;

    /** Area di testo per visualizzare i dati del dendrogramma. */
    @FXML private TextArea dendrogramTextArea;

    /** Etichetta per mostrare messaggi all'utente. */
    @FXML private Label messageLabel;

    /** Riferimento a MainTest per la connessione e la comunicazione con il server. */
    private MainTest mainTest;

    /** Riferimento allo Stage principale per la gestione delle scene. */
    private Stage primaryStage;

    /** Variabile per controllare se il dendrogramma è stato caricato con successo. */
    private boolean isDendrogramLoaded = false;

    /**
     * Imposta l'istanza di MainTest per le operazioni con il server.
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
     * Metodo per inviare il comando di caricamento del dendrogramma dal file.
     * Riceve il nome del file dalla TextField, invia la richiesta al server,
     * e visualizza il contenuto del dendrogramma nella TextArea.
     */
    @FXML
    private void onLoadDendrogramFromFile() {
        String fileName = fileNameTextField.getText();

        if (fileName.isEmpty()) {
            messageLabel.setText("Inserisci il nome del file.");
            return;
        }

        try {
            // Invio della richiesta al server
            mainTest.getOut().writeObject(2);  // Indica l'azione di caricamento file
            mainTest.getOut().writeObject(fileName);

            // Ricezione della risposta del server
            String serverResponse = (String) mainTest.getIn().readObject();

            if ("OK".equals(serverResponse)) {
                String dendrogramData = (String) mainTest.getIn().readObject();
                dendrogramTextArea.setText(dendrogramData);
                messageLabel.setText("Dendrogramma caricato con successo.");
                isDendrogramLoaded = true;
            } else {
                messageLabel.setText("Errore dal server: " + serverResponse);
            }

        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore nel caricamento del file dal server.");
            e.printStackTrace();
        }
    }

    /**
     * Mostra una finestra di dialogo per chiedere all'utente se desidera continuare o terminare.
     * Mostra un avviso se i dati non sono completi, oppure una conferma per un'altra operazione.
     */
    private void askContinueOperation() {
        Alert alert;

        if (!isDendrogramLoaded || fileNameTextField.getText().isEmpty()) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Avviso");
            alert.setHeaderText(null);
            alert.setContentText("Terminare l'inserimento dei dati prima di procedere.");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
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
     * Carica una nuova scena specificata e imposta i controller necessari.
     * Questo metodo consente di navigare tra le scene dell'applicazione.
     *
     * @param fxmlFile il file FXML della scena da caricare
     */
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            // Imposta il controller della nuova scena con MainTest e Stage
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
     * Gestisce il click sul bottone "Termina", aprendo una finestra di dialogo
     * che chiede se continuare con un'altra operazione o chiudere l'applicazione.
     */
    @FXML
    private void onTerminaButtonClick() {
        askContinueOperation();
    }

    /**
     * Chiude la connessione e termina l'applicazione.
     */
    private void closeConnection() {
        mainTest.closeConnection();
        primaryStage.close();
    }
}
