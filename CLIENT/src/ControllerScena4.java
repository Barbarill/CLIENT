import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerScena4 {

    @FXML private TextField depthField;
    @FXML private TextField distanceField;
    @FXML private TextField fileNameField; // Campo per il nome del file di salvataggio
    @FXML private Label messageLabel;
    @FXML private TextArea dendrogramTextArea; // TextArea per visualizzare il dendrogramma

    private MainTest mainTest;
    private Stage primaryStage;

    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void onConfirmButtonClick() {
        try {
            mainTest.getOut().writeObject(1);

            String tableName = mainTest.getTableName();
            mainTest.getOut().writeObject(tableName);

            String depthText = depthField.getText().trim();
            if (!depthText.isEmpty()) {
                int depth = Integer.parseInt(depthText);
                mainTest.getOut().writeObject(depth);
            }

            String distanceText = distanceField.getText().trim();
            if (!distanceText.isEmpty()) {
                int distance = Integer.parseInt(distanceText);
                mainTest.getOut().writeObject(distance);
            }

            String risposta = (String) mainTest.getIn().readObject();

            if ("OK".equals(risposta)) {
                String dendrogramData = (String) mainTest.getIn().readObject();
                dendrogramTextArea.setText(dendrogramData); // Visualizza il dendrogramma nella TextArea
                messageLabel.setText("Dendrogramma caricato correttamente.");
            } else {
                messageLabel.setText("Errore dal server: " + risposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore nella comunicazione con il server.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            messageLabel.setText("Valore non valido. Inserisci un numero.");
        }
    }

    // Metodo per salvare il dendrogramma su file
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
            } else {
                messageLabel.setText("Errore dal server: " + risposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore durante il salvataggio del dendrogramma.");
            e.printStackTrace();
        }
    }
}