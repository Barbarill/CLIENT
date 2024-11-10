import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import client.MainTest;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ControllerScena3 {

    @FXML
    private TextField fileNameTextField;
    @FXML
    private TextArea dendrogramTextArea;
    @FXML
    private Label messageLabel;

    private MainTest mainTest;  // Riferimento a MainTest
    private Stage primaryStage;

    // Metodo per impostare l'istanza di MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    // Metodo per impostare lo Stage principale
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo per inviare il comando di caricamento del dendrogramma dal file
    @FXML
    private void onLoadDendrogramFromFile() {
        String fileName = fileNameTextField.getText();  // Ottieni il nome del file dalla TextField

        if (fileName.isEmpty()) {
            messageLabel.setText("Inserisci il nome del file.");
            return;
        }

        try {
            // Invia il nome del file al server tramite MainTest
            mainTest.getOut().writeObject(2);  // Indica al server l'azione da eseguire
            mainTest.getOut().writeObject(fileName);  // Invio del nome del file

            // Ricevi la risposta dal server
            String serverResponse = (String) mainTest.getIn().readObject();

            if ("OK".equals(serverResponse)) {
                // Ricevi i dati del dendrogramma dal server e visualizzali nella TextArea
                String dendrogramData = (String) mainTest.getIn().readObject();
                dendrogramTextArea.setText(dendrogramData);
                messageLabel.setText("Dendrogramma caricato con successo.");
            } else {
                messageLabel.setText("Errore dal server: " + serverResponse);
            }

        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore nel caricamento del file dal server.");
            e.printStackTrace();
        }
    }

    // Metodo per mostrare la finestra di dialogo
    private void askContinueOperation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(null);
        alert.setContentText("Vuoi fare un'altra operazione? (S/N)");

        ButtonType buttonTypeYes = new ButtonType("S");
        ButtonType buttonTypeNo = new ButtonType("N");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            // Se l'utente sceglie "S", torna alla Scena2
            loadScene("/Scena2.fxml");
        } else {
            // Se l'utente sceglie "N", chiudi la connessione
            closeConnection();
        }
    }

    // Metodo per caricare una nuova scena
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

    // Metodo chiamato dal bottone "Termina"
    @FXML
    private void onTerminaButtonClick() {
        askContinueOperation();
    }

    // Metodo per chiudere la connessione e chiudere la finestra
    private void closeConnection() {
        mainTest.closeConnection();
        primaryStage.close();
    }
}
