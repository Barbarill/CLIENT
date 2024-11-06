import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import client.MainTest;  // Assicurati di importare MainTest

public class ClientApp extends Application {

    private MainTest mainTest;  // Istanza di MainTest

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Passa i parametri richiesti al costruttore
        mainTest = new MainTest("127.0.0.1", 8080);  // IP e porta del server

        // Carica il file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
        AnchorPane root = loader.load();

        // Ottieni il controller e imposta mainTest
        MainSceneBuilderController controller = loader.getController();
        controller.setMainTest(mainTest);

        // Crea la scena e aggiungila alla finestra principale
        Scene scene = new Scene(root);
        primaryStage.setTitle("Client Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
