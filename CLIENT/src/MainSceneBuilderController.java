import client.MainTest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainSceneBuilderController {

    @FXML private StackPane tableNameSection;
    @FXML private StackPane dendrogramSelectionSection;
    @FXML private StackPane inputNumberSection;

    @FXML private TextField tableNameField;
    @FXML private TextField inputNumberField;

    private MainTest mainTest; // Aggiungi questa variabile per memorizzare l'oggetto MainTest

    // Metodo per settare l'istanza di MainTest
    public void setMainTest(MainTest mainTest) {
        this.mainTest = mainTest;
    }

    // Metodo per confermare il nome della tabella
    @FXML
    private void onTableNameConfirm() {
        String tableName = tableNameField.getText();
        // Logica per inviare il nome della tabella al server
        System.out.println("Tabella selezionata: " + tableName);

        // Usa mainTest per chiamare il metodo corretto
        if (mainTest != null) {
            try {
                mainTest.sendTableName(tableName); // Usa sendTableName al posto di setTableName
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Errore durante l'invio del nome della tabella al server.");
            }
        }

        // Nascondi la sezione del nome della tabella e mostra quella per la selezione del dendrogramma
        tableNameSection.setVisible(false);
        dendrogramSelectionSection.setVisible(true);  // Cambia scena qui mostrando la sezione successiva
    }

    // Metodo per selezionare "Carica Dendrogramma da File"
    @FXML
    private void onLoadDendrogramFromFile() {
        System.out.println("Caricamento dendrogramma da file...");

        // Mostra la sezione per inserire il numero (ad esempio, la profondità del dendrogramma)
        dendrogramSelectionSection.setVisible(false);
        inputNumberSection.setVisible(true);  // Mostra la sezione del numero
    }

    // Metodo per selezionare "Apprendi Dendrogramma da Database"
    @FXML
    private void onLearnDendrogramFromDB() {
        System.out.println("Apprendimento dendrogramma dal database...");

        // Mostra la sezione per inserire il numero (ad esempio, la profondità del dendrogramma)
        dendrogramSelectionSection.setVisible(false);
        inputNumberSection.setVisible(true);  // Mostra la sezione del numero
    }

    // Metodo per confermare il numero
    @FXML
    private void onConfirmNumber() {
        String number = inputNumberField.getText();
        System.out.println("Numero inserito: " + number);

        // Qui puoi aggiungere la logica per inviare il numero al server
    }
}