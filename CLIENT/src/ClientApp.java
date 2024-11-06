import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import client.MainTest;

public class ClientApp extends Application {

    private static MainTest mainTest;  // Gestisce la logica di connessione al server (uso statico per condivisione globale)

    @Override
    public void start(Stage primaryStage) {
        try {
            // Crea l'istanza di MainTest se non è stata già creata
            if (mainTest == null) {
                mainTest = new MainTest("127.0.0.1", 8080);  // esempio di creazione solo una volta, con parametri di connessione
            }

            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));

            // Crea il controller e passagli l'istanza di MainTest
            MainSceneBuilderController controller = loader.getController();
            controller.setMainTest(mainTest);  // Passa l'istanza di MainTest al controller

            // Carica e visualizza la scena
            Parent root = loader.load();
            primaryStage.setTitle("Client Interface");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            // Gestisci l'eccezione (ad esempio, stampa un messaggio di errore)
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
