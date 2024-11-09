import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import client.MainTest;

public class ClientApp extends Application {

    private MainTest mainTest;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainTest = new MainTest("127.0.0.1", 8080);  // IP e porta del server

        // Carica la scena iniziale con Scena2.fxml invece di MainScene.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scena2.fxml"));
        AnchorPane root = loader.load();

        // Ottieni il controller della scena iniziale e imposta mainTest e primaryStage
        ControllerScena2 controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setMainTest(mainTest);  // Passa MainTest

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
