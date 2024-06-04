package org.example.client;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JButton[] buttons;
    private boolean myTurn;
    private String myMark;
    private String opponentMark;

    public TicTacToeClient(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        frame = new JFrame("Tic Tac Toe");
        buttons = new JButton[9];
        frame.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonListener(i));
            frame.add(buttons[i]);
        }
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        myTurn = false;

        new Thread(new IncomingReader()).start();
    }

    private void processMessage(String message) {
        String[] tokens = message.split(" ");
        switch (tokens[0]) {
            case "MOVE":
                int index = Integer.parseInt(tokens[1]);
                buttons[index].setText(tokens[2]);
                break;
            case "YOURTURN":
                myTurn = true;
                myMark = tokens[1];
                opponentMark = myMark.equals("X") ? "O" : "X";
                break;
            case "WAIT":
                myTurn = false;
                myMark = tokens[1];
                opponentMark = myMark.equals("X") ? "O" : "X";
                break;
            case "RESET":
                for (JButton button : buttons) {
                    button.setText("");
                }
                break;
        }
    }

    private class ButtonListener implements ActionListener {
        private int index;

        public ButtonListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (myTurn && buttons[index].getText().equals("")) {
                buttons[index].setText(myMark);
                out.println("MOVE " + index + " " + myMark);
                myTurn = false;
            }
        }
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    processMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TicTacToeClient <server address>");
            System.exit(1);
        }
        try {
            new TicTacToeClient(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
