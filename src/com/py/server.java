package com.py;

import java.io.*;
import java.net.*;
import java.util.*;

public class server extends Thread {

    private ServerSocket ss;
    private Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();

    public server() throws IOException {
        //nothing to add here
    }

    /** listen to port and start new Thread
     * */
    private void listen(int port) throws IOException {

        ss = new ServerSocket(port);
        System.out.println("Start listen on: " + ss);

        while (true) {
            Socket s = ss.accept();

            System.out.println("Accepted: " + s);

            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            outputStreams.put(s, dout);

            //start new thread from server
            new serverThread(this, s);
        }
    }

    Enumeration<DataOutputStream> getOutputStreams() {
        return outputStreams.elements();
    }

    /** send one message to all clients
     *  */
    void sendToAll(String message) {

        synchronized(outputStreams) {
            //
            for (Enumeration<DataOutputStream> e = getOutputStreams(); e.hasMoreElements();) {
                //
                DataOutputStream dout = e.nextElement();
                //
                try {
                    dout.writeUTF(message);
                } catch(IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    /** close connection
     * */
    void removeConnection(Socket s) {

        synchronized(outputStreams) {

            System.out.println("Removing connection: "+s);
            outputStreams.remove(s);

            try {
                s.close();
            } catch(IOException ie) {
                System.out.println("Cannot close: "+s);
                ie.printStackTrace();
            }
        }
    }

    public void run(){
        int port = 1;   //random port

        try {
            listen(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}