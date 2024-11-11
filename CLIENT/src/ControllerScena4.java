import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class ControllerScena4 {

    @FXML private TextField depthField;
    @FXML private TextField distanceField;
    @FXML private TextField fileNameField;
    @FXML private Label messageLabel;
    @FXML private TextArea dendrogramTextArea;

    private MainTest mainTest;
    private Stage primaryStage;

    private boolean isConfirmed = false;
    private boolean isSaved = false;

    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void onConfirmButtonClick() {
        messageLabel.setText("");
        isConfirmed = false;

        try {
            // Dichiarazione delle variabili
            int depth;
            int distance;

            // Controllo e parsing del campo profondità
            String depthText = depthField.getText().trim();
            if (!depthText.isEmpty()) {
                try {
                    depth = Integer.parseInt(depthText);
                    if (depth <= 0) {
                        messageLabel.setText("Errore: la profondità deve essere un numero positivo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    messageLabel.setText("Errore: la profondità deve essere un numero intero.");
                    return;
                }
            } else {
                messageLabel.setText("Errore: il campo della profondità è vuoto.");
                return;
            }

            // Controllo e parsing del campo distanza
            String distanceText = distanceField.getText().trim();
            if (!distanceText.isEmpty()) {
                try {
                    distance = Integer.parseInt(distanceText);
                    if (distance <= 0) {
                        messageLabel.setText("Errore: la distanza deve essere un numero positivo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    messageLabel.setText("Errore: la distanza deve essere un numero intero.");
                    return;
                }
            } else {
                messageLabel.setText("Errore: il campo della distanza è vuoto.");
                return;
            }

            // Se tutti i controlli sono passati, ripristina e invia i valori al server
            mainTest.getOut().reset();
            mainTest.getOut().writeObject(1);  // Richiesta di caricamento dendrogramma
            mainTest.getOut().writeObject(mainTest.getTableName());
            mainTest.getOut().writeObject(depth);
            mainTest.getOut().writeObject(distance);

            // Riceve la risposta del server
            String risposta = (String) mainTest.getIn().readObject();
            if ("OK".equals(risposta)) {
                dendrogramTextArea.setText((String) mainTest.getIn().readObject());
                messageLabel.setText("Dendrogramma caricato correttamente.");
                isConfirmed = true;
            } else {
                messageLabel.setText("Errore dal server: " + risposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore nella comunicazione con il server.");
            e.printStackTrace();
        }
    }

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
                isSaved = true;
            } else {
                messageLabel.setText("Errore dal server: " + risposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText("Errore durante il salvataggio del dendrogramma.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onTerminaButtonClick() {
        // Controlla se i campi richiesti sono stati completati
        String fileName = fileNameField.getText().trim();
        String depthText = depthField.getText().trim();
        String distanceText = distanceField.getText().trim();

        if (fileName.isEmpty() || depthText.isEmpty() || distanceText.isEmpty() || !isConfirmed || !isSaved) {
            // Mostra popup di avviso per completare i dati
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Avviso");
            alert.setHeaderText(null);
            alert.setContentText("Terminare l'inserimento dei dati prima di procedere.");
            alert.showAndWait();
        } else {
            // Popup di conferma finale per decidere se continuare o terminare
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText(null);
            alert.setContentText("Vuoi fare un'altra operazione? (S/N)");

            ButtonType buttonTypeYes = new ButtonType("S");
            ButtonType buttonTypeNo = new ButtonType("N");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                loadScene("/Scena2.fxml");
            } else {
                closeConnection();
            }
        }
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            Object controller = loader.getController();
            if (controller instanceof ControllerScena2) {
                ControllerScena2 controllerScena2 = (ControllerScena2) controller;
                controllerScena2.setMainTest(mainTest);
                controllerScena2.setPrimaryStage(primaryStage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Errore nel caricamento della scena.");
        }
    }

    private void closeConnection() {
        mainTest.closeConnection();
        primaryStage.close();
    }
}
