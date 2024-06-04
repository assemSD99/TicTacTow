package org.example.shared;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeServiceImpl extends UnicastRemoteObject implements TicTacToeService {
    private Map<String, Integer> scores;
    private StringBuilder gameHistory;

    public TicTacToeServiceImpl() throws RemoteException {
        scores = new HashMap<>();
        gameHistory = new StringBuilder();
    }

    @Override
    public synchronized void updateScore(String player, int score) throws RemoteException {
        scores.put(player, scores.getOrDefault(player, 0) + score);
    }

    @Override
    public synchronized int getScore(String player) throws RemoteException {
        return scores.getOrDefault(player, 0);
    }

    @Override
    public synchronized void recordGame(String gameDetails) throws RemoteException {
        gameHistory.append(gameDetails).append("\n");
    }

    @Override
    public synchronized String getGameHistory() throws RemoteException {
        return gameHistory.toString();
    }
}
