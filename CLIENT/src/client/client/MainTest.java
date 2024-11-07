package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import utility.Keyboard;


public class MainTest {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public MainTest(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);
		System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    public void sendTableName(String tableName) throws IOException, ClassNotFoundException {
        out.writeObject(0); // Codice di operazione per inviare un nome di tabella
        out.writeObject(tableName);
        String risposta = (String) in.readObject(); // Aspetta la risposta dal server
        System.out.println("Risposta dal server: " + risposta);
    }

    int menu() {
        int answer;
        do {
            System.out.println("(1) Carica Dendrogramma da File");
            System.out.println("(2) Apprendi Dendrogramma da Database");
            System.out.print("Risposta: ");
            answer = Keyboard.readInt();
        } while (answer <= 0 || answer > 2);
        return answer;
    }

    private void loadDataOnServer() throws IOException, ClassNotFoundException {
        boolean flag = false;
        do {
            System.out.println("Nome tabella:");
            String tableName = Keyboard.readString();
            out.writeObject(0);
            out.writeObject(tableName);
            String risposta = (String) (in.readObject());
            if ("OK".equals(risposta)) {
                flag = true;
            } else {
                System.out.println("Errore dal server: " + risposta);
            }
        } while (!flag);
    }

    public void loadDedrogramFromFileOnServer() throws IOException, ClassNotFoundException {
        System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
        String fileName = Keyboard.readString();

        out.writeObject(2);
        out.writeObject(fileName);
        String risposta = (String) in.readObject();
        if ("OK".equals(risposta)) {
            // Ricevi i dati del dendrogramma come stringa
            String dendrogramData = (String) in.readObject();
            System.out.println("Dendrogramma caricato:\n" + dendrogramData);
        } else {
            System.out.println("Errore dal server: " + risposta);
        }
    }

  public  void mineDedrogramOnServer() throws IOException, ClassNotFoundException {
        out.writeObject(1);
        System.out.println("Introdurre la profondità del dendrogramma");
        int depth = Keyboard.readInt();
         // Stampa il tipo di dato di 'depth'
    System.out.println("Tipo di dato di depth: " + ((Object) depth).getClass().getName());
        out.writeObject(depth);
        int dType = -1;
        do {
            System.out.println("Distanza: single-link (1), average-link (2):");
            dType = Keyboard.readInt();
            System.out.println("Tipo di dato di dType: " + ((Object) dType).getClass().getName());
        } while (dType <= 0 || dType > 2);
        out.writeObject(dType);

        String risposta = (String) (in.readObject());
        if ("OK".equals(risposta)) {
            System.out.println(in.readObject());
            System.out.println("Inserire il nome dell'archivio (comprensivo di estensione):");
            String fileName = Keyboard.readString();
            System.out.println("Nome del file inserito: " + fileName);
            out.writeObject(fileName);
            


        // Ricevi l'oggetto dendrogramma appena generato (supponendo che sia di tipo `String` o simile)
        String dendrogramData = (String) in.readObject();  // Supponiamo che il server invii i dati come stringa
        out.writeObject(dendrogramData);  // Invia il dendrogramma generato al server per salvarlo

        String saveResponse = (String) in.readObject();
        System.out.println(saveResponse);
    } else {
        System.out.println("Errore dal server: " + risposta);
    }
}

  

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            System.out.println("Connessione al server chiusa.");
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura delle risorse: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 8080;
        MainTest main = null;
        
        try {
            main = new MainTest(ip, port);
            
            boolean continueRunning = true; // Variabile per controllare il ciclo
            
            while (continueRunning) {
                main.loadDataOnServer(); // Carica i dati dal server
                
                int scelta = main.menu(); // Mostra il menu per la scelta dell'utente
                
                if (scelta == 1) {
                    main.loadDedrogramFromFileOnServer(); // Carica il dendrogramma da un file
                } else if (scelta == 2) {
                    main.mineDedrogramOnServer(); // Apprendi il dendrogramma dal database
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                    continue; // Ritorna all'inizio del ciclo se la scelta è non valida
                }
    
                // Chiedi se l'utente vuole continuare
                System.out.print("Vuoi fare un'altra operazione? (S/N): ");
                String risposta = Keyboard.readString();
                if (risposta.equalsIgnoreCase("N")) {
                    continueRunning = false; // Esci dal ciclo se l'utente non vuole continuare
                }
            }
            
            main.out.writeObject(-1);  
            System.out.println("Richiesta di chiusura inviata al server.");
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore durante la connessione o il trasferimento dei dati: " + e.getMessage());
        } finally {
            if (main != null) main.closeConnection();
        }
    }
}    