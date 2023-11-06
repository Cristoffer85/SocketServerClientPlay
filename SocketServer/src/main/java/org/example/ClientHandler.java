package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

class ClientHandler extends Thread {
        private Socket socket;
        private List<ClientHandler> clients;
        private PrintWriter out;
        private InetSocketAddress clientAddress;
        private String chatName;
        private BufferedWriter chatHistoryWriter;

        public ClientHandler(Socket socket, List<ClientHandler> clients, BufferedWriter chatHistoryWriter) {
                this.socket = socket;
                this.clients = clients;
                this.clientAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                this.chatHistoryWriter = chatHistoryWriter;
                try {
                        out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                        System.out.println("Error creating output stream: " + e.getMessage());
                }
        }

        @Override
        public void run() {
                try {
                        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                        BufferedReader br = new BufferedReader(isr);

                        // Prompt the client for their chat name
                        out.println("Enter your chat name: ");
                        chatName = br.readLine();

                        while (true) {
                                String message = br.readLine();
                                if (message == null || message.equalsIgnoreCase("quit")) {
                                        break;
                                }
                                System.out.println("Message received from " + chatName + ": " + message);

                                // Save the received message to the chat history
                                chatHistoryWriter.write(chatName + " (" + clientAddress + "): " + message);
                                chatHistoryWriter.newLine();
                                chatHistoryWriter.flush();

                                // Broadcast the received message to all clients
                                for (ClientHandler client : clients) {
                                        if (client != this) {
                                                client.sendMessage(chatName + ": " + message);
                                        }
                                }
                        }

                        socket.close();
                        System.out.println("Client disconnected: " + chatName + " (" + clientAddress + ")");
                        clients.remove(this);
                } catch (IOException e) {
                        System.out.println(e.getMessage());
                }
        }

        public void sendMessage(String message) {
                if (out != null) {
                        out.println(message);
                }
        }
}