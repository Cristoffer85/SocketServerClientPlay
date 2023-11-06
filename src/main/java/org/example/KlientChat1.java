package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class KlientChat1 {

    public static void main(String[] args) {
        Socket socket = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter out = null;

        try {
            socket = new Socket("localhost", 4321);
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
            out = new PrintWriter(socket.getOutputStream(), true);

            Scanner scan = new Scanner(System.in);

            String chatName = null;

            // Check if chat name is already entered
            String firstServerMessage = br.readLine();
            if (firstServerMessage.equals("Enter your chat name: ")) {
                System.out.println("Enter your chat name: ");
                chatName = scan.nextLine();
                out.println(chatName);
            } else {
                // Use the received server message as the chat name
                chatName = firstServerMessage;
            }

            System.out.println("Start your chat!");

            // Declare br as final for use in the lambda expression
            final BufferedReader finalBr = br;

            // Create a separate thread for listening to incoming messages
            Thread messageListener = new Thread(() -> {
                try {
                    while (true) {
                        String incomingMessage = finalBr.readLine();
                        if (incomingMessage == null) {
                            break; // Connection closed
                        }
                        System.out.println(incomingMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            messageListener.start();

            while (true) {
                String message = scan.nextLine();
                out.println(message);  // Modify this line

                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
                if (isr != null) isr.close();
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}