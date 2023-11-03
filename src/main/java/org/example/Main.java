package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        //Definiera objekt
        Socket socket = null;
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        //TIlldela värden, i en TryCatch
        try {
            //Socket
            socket = new Socket("localhost", 4321);

            //Stream Reader & Writer
            isr = new InputStreamReader(socket.getInputStream());
            osw = new OutputStreamWriter(socket.getOutputStream());

            //Buffered Reader & Writer
            br = new BufferedReader(isr);
            bw = new BufferedWriter(osw);

            //Skapa en Scanner objekt
            Scanner scan = new Scanner(System.in);

            System.out.println("Starta din chat!");
            //Starta en WHile loop
            while (true) {
                //Ber användaren att skriva ett meddelande
                //Meddelandet skickas iväg via PORT nr
                String message = scan.nextLine();
                bw.write(message);
                bw.newLine();
                bw.flush();

                //Väntar på server respons
                System.out.println(br.readLine());

                //Avsluta applikation om meddelande är 'quit'
                if(message.equalsIgnoreCase("quit")) break;

                if(message.equalsIgnoreCase("serverquit")) break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            //Stäng ner alla kopplingar
            try {
                if (socket != null) socket.close();
                if (isr != null) isr.close();
                if (osw != null) osw.close();
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


    }
}