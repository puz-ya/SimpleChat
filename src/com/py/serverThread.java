package com.py;

import java.io.*;
import java.net.*;

public class serverThread extends Thread
{
    private server mServer;
    private Socket mSocket;

    public serverThread(server server, Socket socket) {
        this.mServer = server;
        this.mSocket = socket;

        //begin this thread
        start();
    }

    public void run() {
        try {
            DataInputStream din = new DataInputStream(mSocket.getInputStream());

            while (true) {
                String message = din.readUTF();

                System.out.println("Got it: " + message);

                mServer.sendToAll(message);
            }
        } catch(IOException ie) {
            ie.printStackTrace();
        } finally {
            mServer.removeConnection(mSocket);
        }
    }
}