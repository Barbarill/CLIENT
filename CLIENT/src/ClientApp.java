import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import client.MainTest;



public class ClientApp extends Application {

    private MainTest mainTest;  // Gestisce la logica di connessione al server

    @Override
    public void start(Stage primaryStage) {
        try {
            // Crea un'istanza di MainTest che sarà utilizzata per la logica
            mainTest = new MainTest();

            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));

            // Crea il controller e passagli l'istanza di MainTest
            MainSceneBuilderController controller = loader.getController();
            controller.setMainTest(mainTest); // Se proprio vuoi avere questa connessione

            Parent root = loader.load();  // Potrebbe lanciare IOException

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
