package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class SocketServerStart {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        List<ClientHandler> clients = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(4321);
            System.out.println("Server startad på port 4321.");

            // Create or open a text file to store chat history
            File chatHistoryFile = new File("chat_history.txt");
            BufferedWriter chatHistoryWriter = new BufferedWriter(new FileWriter(chatHistoryFile, true));

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, clients, chatHistoryWriter);
                clients.add(clientHandler);

                System.out.println("Client connected: " + socket.getRemoteSocketAddress());

                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}