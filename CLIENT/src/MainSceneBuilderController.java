import client.MainTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSceneBuilderController {

    @FXML
    private TextField tableNameField;
    @FXML
    private Label messageLabel;

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
        String tableName = tableNameField.getText();

        if (tableName == null || tableName.trim().isEmpty()) {
            messageLabel.setText("Errore: Nome tabella non valido.");
            return;  // Esci dal metodo se il nome è vuoto
        }

        if (mainTest != null) {
            try {
                // Passa il nome della tabella al server senza passare dalla linea di comando
                sendTableNameToServer(tableName);

                messageLabel.setText("Nome tabella inviato correttamente!");

                // Carica la nuova scena (ad esempio Scena4)
                loadScene("/Scena4.fxml");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                messageLabel.setText("Errore durante l'invio del nome della tabella.");
            }
        } else {
            messageLabel.setText("Errore: mainTest non è stato inizializzato.");
        }
    }

    // Nuovo metodo che invia direttamente il nome della tabella al server
    private void sendTableNameToServer(String tableName) throws IOException, ClassNotFoundException {
        mainTest.getOut().writeObject(0);  // Invio del comando per inviare il nome della tabella
        mainTest.getOut().writeObject(tableName);  // Invio del nome della tabella
        String risposta = (String) mainTest.getIn().readObject();  // Attende la risposta dal server
        if (!"OK".equals(risposta)) {
            throw new IOException("Errore dal server: " + risposta);
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

            // Ottieni il controller della scena caricata e passa le dipendenze
            Object controller = loader.getController();

            if (controller instanceof ControllerScena2) {
                ((ControllerScena2) controller).setMainTest(mainTest);
                ((ControllerScena2) controller).setPrimaryStage(primaryStage);
            } else if (controller instanceof ControllerScena3) {
                ((ControllerScena3) controller).setMainTest(mainTest);
                ((ControllerScena3) controller).setPrimaryStage(primaryStage);
            } else if (controller instanceof ControllerScena4) {
                ((ControllerScena4) controller).setMainTest(mainTest);
                ((ControllerScena4) controller).setPrimaryStage(primaryStage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
