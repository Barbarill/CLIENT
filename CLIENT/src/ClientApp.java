
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.InetAddress;

public class ClientApp extends Application {
    private static String serverIp = "127.0.0.1"; // Imposta l'indirizzo IP del server
    private static int serverPort = 8080; // Imposta la porta del server
    private MainTest mainTest;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bo.fxml"));
        Parent root = loader.load();
        
        // Ottieni il controller dal loader
        MainSceneBuilderController controller = loader.getController();
        controller.setMainTest(mainTest); // Passa l'istanza di MainTest al controller

        primaryStage.setTitle("Client Interface");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
        // Inizializza la connessione al server
        try {
            mainTest = new MainTest(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
