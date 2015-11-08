import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Client {
	private BufferedReader in;
	public static PrintWriter out;
	private HomeClient homeFrame;
	JFrame popUpFrame;
	public static int[] values = new int[6];
	private String clientName, serverName;
	
	public Client() throws NumberFormatException, ScriptException, UnknownHostException, IOException {

		// Layout GUI
		clientName = JOptionPane.showInputDialog("Please enter you name?");
		homeFrame = HomeClient.createAndShowGUI();
	}
	
	public void connectToServer() throws UnknownHostException, IOException{
		// Get the server address from a dialog box.
		String serverAddress = JOptionPane.showInputDialog(homeFrame,
			"Welcome "+clientName+"\nEnter IP Address of the Server:",
			"Welcome to the Rainy Word",
			JOptionPane.QUESTION_MESSAGE);

			// Make connection and initialize streams
			Socket socket = new Socket(serverAddress, 10096);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
		
			
			out.println(clientName);
			serverName = in.readLine();
			GameClient.setServerName(serverName);
			GameClient.setClientName(clientName);
		

	}
	
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.connectToServer();

	}

}
