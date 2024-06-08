package org.example.client;

import org.example.shared.ChatService;
import org.example.shared.TicTacToeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TicTacToeRMIClient {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField chatInput;
    private ChatService chatService;
    private TicTacToeService ticTacToeService;
    private JButton[] buttons;
    private JLabel playerXScoreLabel;
    private JLabel playerOScoreLabel;

    public TicTacToeRMIClient(String serverAddress) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress);
            ticTacToeService = (TicTacToeService) registry.lookup("TicTacToeService");
            chatService = (ChatService) registry.lookup("ChatService");
            initialize();
            startChatListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frame = new JFrame("Tic Tac Toe with Chat");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Panel for the Tic Tac Toe game
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));
        frame.getContentPane().add(gamePanel, BorderLayout.CENTER);

        // Add buttons for Tic Tac Toe
        buttons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 60));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonListener(i));
            gamePanel.add(buttons[i]);
        }

        // Score Panel
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(2, 1));
        JLabel playerXLabel = new JLabel("Player X: ");
        playerXLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerXScoreLabel = new JLabel("0");
        playerXScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel playerOLabel = new JLabel("Player O: ");
        playerOLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerOScoreLabel = new JLabel("0");
        playerOScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scorePanel.add(playerXLabel);
        scorePanel.add(playerXScoreLabel);
        scorePanel.add(playerOLabel);
        scorePanel.add(playerOScoreLabel);
        frame.getContentPane().add(scorePanel, BorderLayout.WEST);

        // Panel for the chat
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(300, 0));
        frame.getContentPane().add(chatPanel, BorderLayout.EAST);

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
        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newGameButton.addActionListener(e -> resetGame());
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 20));
        resetButton.addActionListener(e -> resetScores());
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.addActionListener(e -> System.exit(0));
        controlPanel.add(newGameButton);
        controlPanel.add(resetButton);
        controlPanel.add(exitButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void sendMessage() {
        try {
            String message = chatInput.getText();
            chatService.sendMessage(message);
            chatInput.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startChatListener() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = chatService.receiveMessage();
                    if (message != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
        }
    }

    private void resetScores() {
        playerXScoreLabel.setText("0");
        playerOScoreLabel.setText("0");
    }

    private class ButtonListener implements ActionListener {
        private int index;

        public ButtonListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Logique de traitement des boutons Tic Tac Toe
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TicTacToeRMIClient <server address>");
            System.exit(1);
        }
        new TicTacToeRMIClient(args[0]);
    }
}
