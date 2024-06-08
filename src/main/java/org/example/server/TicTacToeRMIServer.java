// src/main/java/org/example/server/TicTacToeRMIServer.java
package org.example.server;

import org.example.shared.ChatService;
import org.example.shared.ChatServiceImpl;
import org.example.shared.TicTacToeService;
import org.example.shared.TicTacToeServiceImpl;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TicTacToeRMIServer {
    public static void main(String[] args) {
        try {
            TicTacToeService ticTacToeService = new TicTacToeServiceImpl();
            ChatService chatService = new ChatServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("TicTacToeService", ticTacToeService);
            registry.rebind("ChatService", chatService);

            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Service TicTacToe enregistré sur IP : " + ip.getHostAddress());
            System.out.println("Service Chat enregistré sur IP : " + ip.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
