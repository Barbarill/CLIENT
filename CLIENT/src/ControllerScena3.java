

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.IOException;

public class ControllerScena3 {

    @FXML
    private TextField fileNameTextField; // Per ottenere il nome del file inserito dall'utente
    @FXML
    private Label messageLabel;         // Per visualizzare eventuali messaggi di errore o successo
    @FXML
    private Button loadButton;          // Pulsante per procedere al caricamento del dendrogramma

    // Metodo che sarà chiamato quando l'utente clicca sul pulsante "Carica Dendrogramma"
    @FXML
    private void onLoadDendrogramFromFile() {
        String fileName = fileNameTextField.getText().trim(); // Ottieni il nome del file dal campo di testo

        if (fileName.isEmpty()) {
            messageLabel.setText("Errore: Inserisci un nome di file valido.");
        } else {
            // Mostra un messaggio di conferma (senza caricare la scena successiva)
            messageLabel.setText("File selezionato: " + fileName);
            System.out.println("Nome del file inserito: " + fileName);

        }
    }
}