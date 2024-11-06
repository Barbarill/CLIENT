import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import client.MainTest;  // Assicurati che MainTest sia nel package corretto

public class MainSceneBuilderController {

    @FXML
    private TextField tableNameField;

    @FXML
    private Button submitButton;

    @FXML
    private Label feedbackLabel;

    private MainTest mainTest;  // L'istanza di MainTest per la logica di connessione

    @FXML
    public void onSubmitClicked() throws ClassNotFoundException {
        String tableName = tableNameField.getText();
        System.out.println("Nome della tabella inserito: " + tableName);

        if (tableName == null || tableName.trim().isEmpty()) {
            feedbackLabel.setText("Nome della tabella non valido!");
            return;
        }

        if (mainTest != null) {
            try {
                mainTest.sendTableName(tableName);
                feedbackLabel.setText("Tabella inviata correttamente!");
            } catch (IOException e) {
                feedbackLabel.setText("Errore nella connessione al server!");
                e.printStackTrace();
            }
        } else {
            feedbackLabel.setText("Errore: MainTest non inizializzato!");
        }
    }

    // Metodo di iniezione per MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }
}
