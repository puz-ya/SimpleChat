package com.py;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** */
public class client  implements Runnable {

	private JFrame frame = new JFrame("Client");
	private JTextArea textField = new JTextArea(3, 15);
	private JTextArea messageArea = new JTextArea(10, 15);
	private JButton sendButton = new JButton();
	private JButton save_hist_Button = new JButton();
	private JPanel b_panel = new JPanel();
	private JMenu jMenu1;
	private JMenu jMenu2;
	private JMenuBar jMenuBar;
	private JMenuItem jMenuItem1;
	private JMenuItem jMenuItem2;

    private DataOutputStream dout;
    private DataInputStream din;
    
    public client(){
    	
		frame.setLocation(400,200);
    	
		//Menu with 2 lists
		jMenuBar = new JMenuBar();
		jMenu1 = new JMenu();
		jMenu2 = new JMenu();
		jMenuItem1 = new JMenuItem();
		jMenuItem2 = new JMenuItem();

		//
		jMenu1.setText("Main");
		jMenu2.setText("Help");
		
		jMenuItem1.setText("Close");
		jMenuItem2.setText("About");

		//set actions to menu buttons
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	jMenuItem2ActionPerformed(evt);
            }
        });
        
        //Get it all together
        jMenu1.add(jMenuItem1);
		jMenu2.add(jMenuItem2);
		jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);
        frame.setJMenuBar(jMenuBar);

        //Add textareas
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setFont(new Font("monospaced", Font.PLAIN, 14));
		textField.setLineWrap(true);
		textField.setFont(new Font("monospaced", Font.PLAIN, 14));

        frame.getContentPane().add(new JScrollPane(messageArea), "North");
        
        //two user buttons
        sendButton.setText("Send");
        save_hist_Button.setText("Save");
        
        //action - Send
        sendButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		sendButtonActionPerformed(evt);
            }
        });

        //action - Save
        save_hist_Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
				save_hist_ButtonActionPerformed(evt);
            }
        });
        
        b_panel.setLayout(new GridLayout(1, 3, 1, 1));
        b_panel.add(textField);
        b_panel.add(sendButton);
        b_panel.add(save_hist_Button);
	    
        frame.getContentPane().add(b_panel, "Center");
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

	/**
	 * Send text from textfield to the Chat
	 */
    private void sendButtonActionPerformed(ActionEvent evt) 
    {
    	try { dout.writeUTF(textField.getText()); } 
    	catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        textField.setText("");
    }

	/** Save all chat messages to file
	 * */
	private void save_hist_ButtonActionPerformed(ActionEvent evt){
  		FileWriter fw;
  		Date date = new Date(System.currentTimeMillis());

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		String customerDate = year + "-" + month + "-" + day;

		try{
			fw = new FileWriter("D:\\Java\\"+ customerDate +".txt");
		}
		catch(IOException exc){
			System.out.println("FileWriter error!");
			return;
		}

		try{
			//customerDate = messageArea.getText();
			//customerDate = customerDate.replaceAll("\n", "\r\n");
			fw.write(messageArea.getText());
		}
		catch(IOException exc){
			System.out.println("Cannot write!");
			return;
		}

		try{
			fw.close();
		}
		catch(IOException exc){
			System.out.println("FileWriter was not closed!");
			return;
		}

		System.out.println("Successfully saved.");
	}

	/** Menu - Close
	 * */
	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		frame.dispose();
	}
		
	/** Menu - About
	 * */
	private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
    	String s;
    	s ="Start chat with this app today! \n";
    	s +="1) Start Server \n";
    	s +="2) Start Client, write message, click Send button - and there you are! \n";
    	JOptionPane.showMessageDialog(frame, s,"About", JOptionPane.INFORMATION_MESSAGE);
	}

	/** main cycle
	 * */
	public void run(){
		try{
			String serverAddress = "localhost";
			Socket socket = new Socket(serverAddress, 1);

			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			Locale local = new Locale("ru","RU");

			while (true) {

				DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
				String line = din.readUTF();
				messageArea.append("Message (" + df.format(new Date()) + "): " + line + "\r\n");
			}
		} catch(IOException e){}
	}
}
