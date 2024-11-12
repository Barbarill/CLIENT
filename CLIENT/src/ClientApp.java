import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.MainTest;

/**
 * La classe ClientApp rappresenta l'applicazione client che si connette al server,
 * carica l'interfaccia utente FXML e imposta il contesto per il controller.
 * Estende la classe Application di JavaFX per avviare un'applicazione grafica.
 */
public class ClientApp extends Application {

    /** Istanza di MainTest per la connessione e la comunicazione con il server. */
    private MainTest mainTest;

    /**
     * Metodo di avvio dell'applicazione JavaFX.
     * Inizializza la connessione al server, carica la scena iniziale da un file FXML
     * e imposta il contesto per il controller.
     *
     * @param primaryStage lo Stage principale dell'applicazione
     * @throws Exception se si verifica un errore durante il caricamento del file FXML o la connessione al server
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainTest = new MainTest("127.0.0.1", 8080);  // IP e porta del server

        // Carica la scena iniziale con Scena2.fxml invece di MainScene.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scena2.fxml"));
        Parent root = loader.load();

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

    /**
     * Metodo main per avviare l'applicazione client.
     *
     * @param args gli argomenti della linea di comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}
