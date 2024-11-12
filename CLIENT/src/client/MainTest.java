package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * La classe MainTest è responsabile della connessione al server, della gestione del menu delle operazioni
 * e della comunicazione con il server per caricare o apprendere un dendrogramma basato sui dati del database.
 */
public class MainTest {

    /** Variabile di flusso per l'output verso il server */
    private ObjectOutputStream out;

    /** Variabile di flusso per l'input dal server */
    private ObjectInputStream in;

    /** Nome della tabella da utilizzare per l'operazione sul server */
    private String tableName;

    /**
     * Imposta il nome della tabella da utilizzare per le operazioni sul server.
     *
     * @param tableName Il nome della tabella
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Restituisce il nome della tabella.
     *
     * @return Il nome della tabella
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Costruttore per inizializzare la connessione al server tramite indirizzo IP e porta.
     *
     * @param ip L'indirizzo IP del server
     * @param port La porta del server
     * @throws IOException Se si verificano errori di input/output durante la connessione
     */
    public MainTest(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);
        Socket socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Restituisce l'oggetto di output per inviare dati al server.
     *
     * @return L'oggetto ObjectOutputStream per la comunicazione con il server
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /**
     * Restituisce l'oggetto di input per ricevere dati dal server.
     *
     * @return L'oggetto ObjectInputStream per la comunicazione con il server
     */
    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * Mostra il menu delle operazioni disponibili e restituisce la scelta dell'utente.
     *
     * @return La scelta dell'utente (1 o 2)
     */
    private int menu() {
        int answer;
        do {
            System.out.println("(1) Carica Dendrogramma da File");
            System.out.println("(2) Apprendi Dendrogramma da Database");
            System.out.print("Risposta: ");
            answer = Keyboard.readInt();
        } while (answer <= 0 || answer > 2);
        return answer;
    }

    /**
     * Permette di caricare i dati dal server utilizzando il nome della tabella fornito dall'utente.
     * Il server verifica la validità della tabella e la carica se valida.
     *
     * @throws IOException Se si verificano errori durante la comunicazione
     * @throws ClassNotFoundException Se si verifica un errore nel deserializzare la risposta dal server
     */
    public void loadDataOnServer() throws IOException, ClassNotFoundException {
        boolean flag = false;
        do {
            System.out.println("Nome tabella:");
            String tableName = Keyboard.readString();
            out.writeObject(0);  // comando per caricare i dati
            out.writeObject(tableName);
            String risposta = (String) in.readObject();
            if ("OK".equals(risposta)) {
                flag = true;
            } else {
                System.out.println("Errore dal server: " + risposta);
            }
        } while (!flag);
    }

    /**
     * Permette di caricare un dendrogramma da un file salvato.
     *
     * @throws IOException Se si verificano errori durante la comunicazione
     * @throws ClassNotFoundException Se si verifica un errore nel deserializzare la risposta dal server
     */
    private void loadDedrogramFromFileOnServer() throws IOException, ClassNotFoundException {
        System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
        String fileName = Keyboard.readString();
        out.writeObject(2);  // comando per caricare dal file
        out.writeObject(fileName);
        String risposta = (String) in.readObject();
        if ("OK".equals(risposta)) {
            String dendrogramData = (String) in.readObject();
            System.out.println("Dendrogramma caricato:\n" + dendrogramData);
        } else {
            System.out.println("Errore dal server: " + risposta);
        }
    }

    /**
     * Permette di generare un dendrogramma tramite il server, utilizzando una tabella e la profondità specificata dall'utente.
     * Il tipo di distanza (single-link o average-link) viene selezionato dall'utente.
     *
     * @throws IOException Se si verificano errori durante la comunicazione
     * @throws ClassNotFoundException Se si verifica un errore nel deserializzare la risposta dal server
     */
    private void mineDedrogramOnServer() throws IOException, ClassNotFoundException {
        out.writeObject(1);  // comando per generare il dendrogramma
        System.out.println("Inserisci il nome della tabella da utilizzare:");
        String tableName = Keyboard.readString();
        out.writeObject(tableName);

        // Gestione della profondità
        int depth = -1;
        while (depth == -1) {
            System.out.println("Introdurre la profondità del dendrogramma (deve essere un numero positivo):");
            try {
                depth = Keyboard.readInt();
                if (depth <= 0) {
                    System.out.println("Errore: La profondità deve essere un numero positivo. Riprova.");
                    depth = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore: La profondità deve essere un numero intero. Riprova.");
            }
        }
        out.writeObject(depth);

        // Gestione del tipo di distanza
        int dType = -1;
        while (dType <= 0 || dType > 2) {
            System.out.println("Distanza: single-link (1), average-link (2):");
            try {
                dType = Keyboard.readInt();
                if (dType <= 0 || dType > 2) {
                    System.out.println("Errore: Devi scegliere tra 1 (single-link) o 2 (average-link). Riprova.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un numero valido per il tipo di distanza.");
            }
        }
        out.writeObject(dType);

        String risposta = (String) in.readObject();
        if ("OK".equals(risposta)) {
            String dendrogramData = (String) in.readObject();
            System.out.println("Dendrogramma generato:\n" + dendrogramData);

            System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
            String fileName = Keyboard.readString();
            out.writeObject(fileName);
            String saveResponse = (String) in.readObject();
            System.out.println(saveResponse);
        } else {
            System.out.println("Errore dal server: " + risposta);
        }
    }

    /**
     * Chiude la connessione con il server e libera le risorse di input/output.
     */
    public void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            System.out.println("Connessione al server chiusa.");
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura delle risorse: " + e.getMessage());
        }
    }

    /**
     * Metodo principale che gestisce la connessione con il server, il menu delle operazioni e la selezione dell'utente.
     *
     * @param args Argomenti della linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 8080;
        MainTest main = null;

        try {
            main = new MainTest(ip, port);
            boolean continueRunning = true;

            while (continueRunning) {
                int scelta = main.menu();

                if (scelta == 1) {
                    main.loadDedrogramFromFileOnServer();
                } else if (scelta == 2) {
                    main.mineDedrogramOnServer();
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                    continue;
                }

                System.out.print("Vuoi fare un'altra operazione? (S/N): ");
                String risposta = Keyboard.readString();
                if (risposta.equalsIgnoreCase("N")) {
                    continueRunning = false;
                }
            }

            main.out.writeObject(-1); // comando di chiusura
            System.out.println("Richiesta di chiusura inviata al server.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore durante la connessione o il trasferimento dei dati: " + e.getMessage());
        } finally {
            if (main != null) main.closeConnection();
        }
    }
}