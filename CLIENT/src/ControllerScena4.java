import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerScena4 {

    @FXML private TextField depthField;    // Campo per la profondità
    @FXML private TextField distanceField; // Campo per la distanza
    @FXML private Label messageLabel;      // Etichetta per messaggi di errore o successi
    @FXML private TextField fileNameField; // Campo per il nome del file

    private MainTest mainTest;  // Riferimento a MainTest
    private Stage primaryStage;

    // Metodo per impostare MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo chiamato quando l'utente preme il bottone "Conferma"
    @FXML
    private void onConfirmButtonClick() {
        try {
            // Invia il codice per avviare il processo del dendrogramma
            mainTest.getOut().writeObject(1);

            // Leggi la profondità e inviala al server
            String depthText = depthField.getText().trim();
            if (!depthText.isEmpty()) {
                int depth = Integer.parseInt(depthText);
                mainTest.getOut().writeObject(depth);
            }

            // Leggi il tipo di distanza e invialo al server
            String distanceText = distanceField.getText().trim();
            if (!distanceText.isEmpty()) {
                int distance = Integer.parseInt(distanceText);
                mainTest.getOut().writeObject(distance);
            }

            // Ricevi la risposta dal server
            String risposta = (String) mainTest.getIn().readObject();

            if ("OK".equals(risposta)) {
                // Il server ha accettato i dati, ora ricevi il dendrogramma
                String dendrogramData = (String) mainTest.getIn().readObject();

                if (dendrogramData != null && !dendrogramData.isEmpty()) {
                    // Visualizza il dendrogramma ricevuto dal server
                    messageLabel.setText("Dendrogramma caricato:\n" + dendrogramData);

                    // Ora chiediamo all'utente di inserire il nome del file per salvarlo
                    messageLabel.setText(messageLabel.getText() + "\nOra inserisci il nome del file per salvare il dendrogramma.");

                    // Creiamo un nuovo campo di input per il nome del file
                    // Qui l'utente potrà inserire il nome e fare clic su un altro bottone per salvare
                    // (creeremo un secondo bottone per questa operazione)
                } else {
                    messageLabel.setText("Errore nel caricamento del dendrogramma.");
                }

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
    // Metodo per gestire il salvataggio del dendrogramma
    @FXML
    private void onSaveButtonClick() {
        try {
            // Leggi il nome del file dal campo di input
            String fileName = fileNameField.getText().trim();

            // Verifica che il nome del file non sia vuoto
            if (fileName.isEmpty()) {
                messageLabel.setText("Il nome del file non può essere vuoto.");
                return;
            }

            // Invia il nome del file al server per il salvataggio
            mainTest.getOut().writeObject(fileName);

            // Ricevi la conferma dal server
            String saveResponse = (String) mainTest.getIn().readObject();
            messageLabel.setText(saveResponse);  // Mostra la risposta del server (es. "Salvataggio completato")

        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore nel salvataggio del file.");
            e.printStackTrace();
        }
    }
}
