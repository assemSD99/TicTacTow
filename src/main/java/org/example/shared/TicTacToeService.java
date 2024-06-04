package org.example.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface TicTacToeService extends Remote {
    void updateScore(String player, int score) throws RemoteException;
    int getScore(String player) throws RemoteException;
    void recordGame(String gameDetails) throws RemoteException;
    String getGameHistory() throws RemoteException;
}
