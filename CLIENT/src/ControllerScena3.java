import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import client.MainTest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.stage.Stage;

public class ControllerScena3 {

    @FXML private TextField fileNameTextField;
    @FXML private TextArea dendrogramTextArea;
    @FXML private Label messageLabel;

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
    // In ControllerScena3
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

}