package org.example.client;

import org.example.shared.TicTacToeService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TicTacToeRMIClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TicTacToeRMIClient <server address>");
            System.exit(1);
        }
        try {
            Registry registry = LocateRegistry.getRegistry(args[0]);
            TicTacToeService service = (TicTacToeService) registry.lookup("TicTacToeService");

            // Exemple d'utilisation
            service.updateScore("Alice", 1);
            int score = service.getScore("Alice");
            System.out.println("Score d'Alice: " + score);

            service.recordGame("Alice vs Bob: Alice won");
            String history = service.getGameHistory();
            System.out.println("Historique des parties: \n" + history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
