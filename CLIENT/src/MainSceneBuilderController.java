import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainSceneBuilderController {

    @FXML
    private TextField tableNameField;
    @FXML
    private Label messageLabel;

    private MainTest mainTest;  // Aggiungi un campo per MainTest

    // Metodo per impostare MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    // Metodo che viene chiamato quando si clicca sul pulsante "Conferma"
    @FXML
    private void onSubmitClicked() {
        String tableName = tableNameField.getText();

        if (tableName.isEmpty()) {
            messageLabel.setText("Inserisci un nome per la tabella.");
        } else {
            messageLabel.setText("Nome Tabella: " + tableName);

            // Esegui qualche operazione usando mainTest
            // Ad esempio: mainTest.eseguiOperazione(tableName);
        }
    }


}
