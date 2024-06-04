package org.example.server;

import java.net.*;
import java.io.*;
import java.util.*;

public class TicTacToeServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private String[] board;
    private int currentPlayer;

    public TicTacToeServer() {
        clients = new ArrayList<>();
        board = new String[9];
        Arrays.fill(board, "");
        currentPlayer = 0;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Serveur lancé sur le port " + PORT);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this, clients.size());
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                if (clients.size() == 2) {
                    clients.get(0).sendMessage("YOURTURN X");
                    clients.get(1).sendMessage("WAIT O");
                }
            } catch (IOException e) {
                System.err.println("Erreur d'acceptation de connexion: " + e.getMessage());
            }
        }
    }

    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public synchronized void processMove(int index, int player) {
        if (board[index].equals("")) {
            board[index] = player == 0 ? "X" : "O";
            broadcast("MOVE " + index + " " + board[index]);
            currentPlayer = (currentPlayer + 1) % 2;

            if (clients.size() == 2) {
                clients.get(currentPlayer).sendMessage("YOURTURN " + (currentPlayer == 0 ? "X" : "O"));
                clients.get((currentPlayer + 1) % 2).sendMessage("WAIT " + (currentPlayer == 0 ? "X" : "O"));
            }
        }
    }

    public static void main(String[] args) {
        try {
            new TicTacToeServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
