// src/main/java/org/example/shared/ChatServiceImpl.java
package org.example.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private List<String> messages;

    public ChatServiceImpl() throws RemoteException {
        messages = new ArrayList<>();
    }

    @Override
    public synchronized void sendMessage(String message) throws RemoteException {
        messages.add(message);
    }

    @Override
    public synchronized String receiveMessage() throws RemoteException {
        return messages.size() > 0 ? messages.remove(0) : null;
    }
}
