import client.MainTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSceneBuilderController {

    @FXML
    private TextField tableNameField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label tableNameLabel;


    private MainTest mainTest;  // Riferimento a MainTest
    private Stage primaryStage; // Riferimento allo Stage principale

    // Metodo per impostare l'istanza di MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    // Metodo per impostare lo Stage principale
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo per inviare il nome della tabella e cambiare scena
    @FXML
    private void onTableNameConfirm() {
        // Ottieni il nome della tabella dal TextField
        String tableName = tableNameField.getText();
        System.out.println("Tabella selezionata: " + tableName);

        // Verifica se il mainTest è stato correttamente inizializzato
        if (mainTest != null) {
            try {
                // Invia il nome della tabella al server
                mainTest.sendTableName(tableName);

                // Se tableNameLabel non è null, imposta il testo
                if (tableNameLabel != null) {
                    tableNameLabel.setText("Nome della tabella: " + tableName);
                } else {
                    System.out.println("tableNameLabel non è stato trovato!");
                }

                // Imposta un messaggio di successo nel messageLabel
                messageLabel.setText("Nome tabella inviato correttamente!");

                // Cambia alla scena successiva (Scena2)
                loadScene("/Scena2.fxml");

            } catch (IOException | ClassNotFoundException e) {
                // Stampa l'errore nel log e aggiorna messageLabel con il messaggio di errore
                e.printStackTrace();
                messageLabel.setText("Errore durante l'invio del nome della tabella.");
            }
        } else {
            messageLabel.setText("Errore: mainTest non è stato inizializzato.");
        }
    }


    // Metodo per caricare e passare alla scena specificata
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.getScene().setRoot(root); // Cambia la scena
            } else {
                System.out.println("Errore: primaryStage è null");
            }

            // Inizializza il controller della scena successiva
            ControllerScena2 controller = loader.getController();
            controller.setMainTest(mainTest);  // Passa l'istanza di MainTest
            controller.setPrimaryStage(primaryStage);  // Passa lo Stage
            controller.setTableName(tableNameField.getText()); // Passa il nome della tabella

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
