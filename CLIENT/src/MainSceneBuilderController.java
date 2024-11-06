import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainSceneBuilderController {

    @FXML
    private TextField tableNameField;  // FXML ID per il TextField
    @FXML
    private Label messageLabel;  // FXML ID per il Label

    // Metodo che viene chiamato quando si clicca sul pulsante "Conferma"
    @FXML
    private void onSubmitClicked() {
        String tableName = tableNameField.getText();

        // Mostra un messaggio nel label
        if (tableName.isEmpty()) {
            messageLabel.setText("Inserisci un nome per la tabella.");
        } else {
            messageLabel.setText("Nome Tabella: " + tableName);
        }
    }
}