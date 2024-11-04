

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


    public class MainSceneBuilderController {

        @FXML
        private TextField tableNameField;
    
        @FXML
        private Button submitButton;
    
        @FXML
        public void onSubmitClicked() {
            String tableName = tableNameField.getText();
            System.out.println("Nome della tabella inserito: " + tableName);
            // Qui puoi usare il valore per inviare la richiesta al server
        }
    }