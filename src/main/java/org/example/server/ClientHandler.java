package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

class ClientHandler implements Runnable {
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

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message from client: " + message);
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
