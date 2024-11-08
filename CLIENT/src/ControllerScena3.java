
import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerScena3 {

    @FXML
    private TextField fileNameTextField; // Per ottenere il nome del file inserito dall'utente
    @FXML
    private Label messageLabel;         // Per visualizzare eventuali messaggi di errore o successo
    @FXML
    private Button loadButton;          // Pulsante per procedere al caricamento del dendrogramma

    private MainTest mainTest;  // Riferimento a MainTest
    private Stage primaryStage;

    // Metodo per impostare il riferimento a MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void onLoadDendrogramFromFile() {
        String fileName = fileNameTextField.getText().trim();

        if (fileName.isEmpty()) {
            messageLabel.setText("Errore: Inserisci un nome di file valido.");
        } else {
            try {
                // Invia il nome del file al server e ottieni i dati del dendrogramma
                mainTest.getOut().writeObject(2);  // Codice operazione per caricare dendrogramma
                mainTest.getOut().writeObject(fileName);
                String risposta = (String) mainTest.getIn().readObject();

                if ("OK".equals(risposta)) {
                    // Leggi i dati del dendrogramma dal server e visualizzali
                    String dendrogramData = (String) mainTest.getIn().readObject();
                    messageLabel.setText("Dendrogramma caricato:\n" + dendrogramData);
                } else {
                    messageLabel.setText("Errore dal server: " + risposta);
                }
            } catch (IOException | ClassNotFoundException e) {
                messageLabel.setText("Errore durante il caricamento del dendrogramma.");
                e.printStackTrace();
            }
        }
    }
}