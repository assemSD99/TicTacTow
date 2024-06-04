package org.example.server;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private TicTacToeServer server;
    private PrintWriter out;
    private int player;

    public ClientHandler(Socket socket, TicTacToeServer server, int player) {
        this.socket = socket;
        this.server = server;
        this.player = player;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            // Initial turn messages are now sent by the server when the second client connects

            String message;
            while ((message = in.readLine()) != null) {
                String[] tokens = message.split(" ");
                if (tokens[0].equals("MOVE")) {
                    int index = Integer.parseInt(tokens[1]);
                    server.processMove(index, player);
                }
            }
        } catch (SocketException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
