import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainSceneBuilderController {

    @FXML
    private TextField tableNameField;

    @FXML
    private Button submitButton;

    // Facoltativo, se vuoi lasciare MainTest nel controller
    private MainTest mainTest;

    @FXML
    public void onSubmitClicked() {
        String tableName = tableNameField.getText();
        System.out.println("Nome della tabella inserito: " + tableName);

        // Qui puoi gestire la logica di invio della tabella tramite ClientApp
        // Ad esempio, possiamo inviare il nome della tabella tramite una classe principale

        // Se vuoi usare mainTest dal controller (ma se non lo vuoi puoi farlo direttamente in ClientApp)
        if (mainTest != null) {
            mainTest.sendTableName(tableName); // invio al server tramite MainTest
        }
    }

    // Opzionale, se vuoi avere un metodo di iniezione per MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }
}
