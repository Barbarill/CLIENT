import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml")); // Assicurati di avere il percorso giusto
        AnchorPane root = loader.load();

        // Crea la scena con il layout radice
        Scene scene = new Scene(root);

        // Imposta il titolo della finestra
        primaryStage.setTitle("Client Application");

        // Aggiungi la scena alla finestra e mostra
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}