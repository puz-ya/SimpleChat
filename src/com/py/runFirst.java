package com.py;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class runFirst {
	
	private JFrame mFrame;
	
	public static void main(String[] args) throws Exception {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
	    catch (Exception unused) {
			unused.printStackTrace();
		}

		runFirst CM = new runFirst();
        CM.mFrame.setVisible(true);
    }

    /** start main frame with 2 buttons - client and server
     * */
	public runFirst(){

		//Create GUI
		mFrame = new JFrame("Admin Panel");
		JButton serv = new JButton("Server");
		JButton cl = new JButton("Client");

		//Panel with 2 buttons in a row
		JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new GridLayout(1, 2, 2, 2));
	    buttonPanel.add(serv);
	    buttonPanel.add(cl);
	    
		JPanel content = new JPanel();
        content.setLayout(new BorderLayout(4, 4));
        content.add(buttonPanel, BorderLayout.CENTER);

        //will start Server thread
		serv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	server t;
				try {
					t = new server();
					t.start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
            }
        });

		//will start client window in a new thread
		cl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		Thread t= new Thread(new client());
					t.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
            }
        });

        setFrameFromSettings();

        mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mFrame.getContentPane().add(content,"Center");
	}

	/** read settings file and set Frame dimensions
     * */
	private void setFrameFromSettings(){
        //read settings from txt file
        FileReader fr;
        BufferedReader br;
        try{
            fr = new FileReader("D:\\Java\\test.txt");
            br = new BufferedReader(fr);
        }
        catch(FileNotFoundException exc){
            System.out.println("File not found!");
            return;
        }

        String s;
        ArrayList<String> settings = new ArrayList<>();
        try{
            while ((s = br.readLine()) != null)	{
                settings.add(s);
            }
        }
        catch(IOException exc){
            System.out.println("Cannot readLine!");
        }

        try{
            fr.close();
        }
        catch(IOException exc){
            System.out.println("FileReader was not closed!");
        }

        //set frame setting according .txt settings file
        mFrame.setSize(Integer.parseInt(settings.get(0)),Integer.parseInt(settings.get(1)));
        mFrame.setLocation(Integer.parseInt(settings.get(2)),Integer.parseInt(settings.get(3)));
        mFrame.setResizable(Boolean.parseBoolean(settings.get(4)));

    }
	
}
