package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        //Definiera objekt
        Socket socket = null;
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        ServerSocket serverSocket = null;

        //Claim vald portnummer på nuvarande server
        try {
            serverSocket = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        boolean active = true;
        //Server-loop
        while(active) {
            try {
                socket = serverSocket.accept();

                //Stream Reader & Writer
                isr = new InputStreamReader(socket.getInputStream());
                osw = new OutputStreamWriter(socket.getOutputStream());

                //Buffered Reader & Writer
                br = new BufferedReader(isr);
                bw = new BufferedWriter(osw);

                //Start av chat med ny klient
                while (true) {
                    String message = br.readLine();
                    System.out.println(String.format("Klientens meddelande: %s", message));

                    bw.write("Message Recieved");
                    bw.newLine();
                    bw.flush();

                    //Kollar om klienten vill stänga kopplingen
                    if (message.equalsIgnoreCase("quit")) break;

                    if (message.equalsIgnoreCase("serverquit")) {
                        active = false;
                        break;
                    }
                }

                //Stäng ner kopplingar
                socket.close();
                isr.close();
                osw.close();
                br.close();
                bw.close();


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}