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
    private JLabel playerXScoreLabel;
    private JLabel playerOScoreLabel;
    private int playerXScore;
    private int playerOScore;
    private JTextArea chatArea;
    private JTextField chatInput;

    public TicTacToeClient(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        frame = new JFrame("Tic Tac Toe with Chat");
        buttons = new JButton[9];
        frame.setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonListener(i));
            gamePanel.add(buttons[i]);
        }

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(3, 2));
        scorePanel.add(new JLabel("Player X:"));
        playerXScoreLabel = new JLabel("0");
        scorePanel.add(playerXScoreLabel);
        scorePanel.add(new JLabel("Player O:"));
        playerOScoreLabel = new JLabel("0");
        scorePanel.add(playerOScoreLabel);

<<<<<<< Updated upstream
=======
        // Chat Panel
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatInput = new JTextField();
        chatInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        chatPanel.add(chatInput, BorderLayout.SOUTH);

        // Control Panel
>>>>>>> Stashed changes
        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> resetGame());
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetScores());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        controlPanel.add(newGameButton);
        controlPanel.add(resetButton);
        controlPanel.add(exitButton);

<<<<<<< Updated upstream
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setSize(400, 450);
=======
        // Layout the frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.WEST);
        frame.add(chatPanel, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setSize(800, 700);
>>>>>>> Stashed changes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        myTurn = false;

        new Thread(new IncomingReader()).start();
    }

    private void processMessage(String message) {
        System.out.println("Message from server: " + message);
        String[] tokens = message.split(" ");
        switch (tokens[0]) {
            case "MOVE":
                int index = Integer.parseInt(tokens[1]);
                buttons[index].setText(tokens[2]);
                checkForWin();
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
            case "CHAT":
                chatArea.append(message.substring(5) + "\n");
                break;
        }
    }

    private void checkForWin() {
        // Check rows, columns and diagonals for a win
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = buttons[i].getText();
        }

        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].isEmpty()) {
                announceWinner(board[i][0]);
                return;
            }
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].isEmpty()) {
                announceWinner(board[0][i]);
                return;
            }
        }

        // Check diagonals
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].isEmpty()) {
            announceWinner(board[0][0]);
            return;
        }
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].isEmpty()) {
            announceWinner(board[0][2]);
        }
    }

    private void announceWinner(String winner) {
        String message = "Player " + winner + " wins!";
        JOptionPane.showMessageDialog(frame, message, "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
        if (winner.equals("X")) {
            playerXScore++;
            playerXScoreLabel.setText(String.valueOf(playerXScore));
        } else {
            playerOScore++;
            playerOScoreLabel.setText(String.valueOf(playerOScore));
        }
        resetGame();
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
        }
        out.println("RESET");
    }

    private void resetScores() {
        playerXScore = 0;
        playerOScore = 0;
        playerXScoreLabel.setText("0");
        playerOScoreLabel.setText("0");
        resetGame();
    }

    private void sendMessage() {
        String message = chatInput.getText();
        out.println("CHAT " + message);
        chatInput.setText("");
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
                checkForWin();
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
