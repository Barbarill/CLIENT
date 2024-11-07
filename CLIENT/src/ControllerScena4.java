import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControllerScena4 {

    @FXML private TextField depthField;  // Campo per la profondità
    @FXML private TextField distanceField;  // Campo per la distanza

    // Metodo chiamato quando l'utente preme "Conferma"
    @FXML
    private void onConfirmButtonClick() {
        try {
            // Leggi i valori inseriti nei campi di testo
            int depth = Integer.parseInt(depthField.getText());  // La profondità
            int distance = Integer.parseInt(distanceField.getText());  // La distanza

            // Controllo dei valori
            if (depth <= 0) {
                showError("La profondità deve essere maggiore di zero.");
                return;
            }
            if (distance != 1 && distance != 2) {
                showError("La distanza deve essere 1 (single-link) o 2 (average-link).");
                return;
            }

            // Esegui le operazioni con i dati inseriti, ad esempio chiamando un metodo del server

            // Successo
            System.out.println("Profondità: " + depth);
            System.out.println("Distanza: " + (distance == 1 ? "single-link" : "average-link"));

            // Dopo il processo, puoi anche tornare alla Scena2 o un'altra scena
            // loadScene("/Scena2.fxml");

        } catch (NumberFormatException e) {
            showError("I valori inseriti non sono validi.");
        }
    }

    // Metodo per mostrare un messaggio di errore
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
