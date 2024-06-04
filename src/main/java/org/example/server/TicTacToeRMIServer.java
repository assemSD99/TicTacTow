package org.example.server;

import org.example.shared.TicTacToeService;
import org.example.shared.TicTacToeServiceImpl;

import java.net.InetAddress;



import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TicTacToeRMIServer {
    public static void main(String[] args) {
        try {
            TicTacToeService service = new TicTacToeServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("TicTacToeService", service);
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Service TicTacToe enregistré sur IP : " + ip.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

