import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Client {
	private BufferedReader in;
	public static PrintWriter out;
	private int serverTime;
	private int serverTime2;
	private int clientTime;
	private int clientTime2;
	private static int serverScore=0;	
	private static int clientScore=0;
	private Home homeFrame;
	public static int[] values = new int[6];
	private String clientName, serverName;
	
	public Client() throws NumberFormatException, ScriptException {

		// Layout GUI
		clientName = JOptionPane.showInputDialog("Please enter you name?");
		homeFrame = Home.createAndShowGUI();
		
	}
	
	public void connectToServer() throws UnknownHostException, IOException{
		// Get the server address from a dialog box.
		String serverAddress = JOptionPane.showInputDialog(
			homeFrame,
			"Enter IP Address of the Server:",
			"Welcome to the Capitalization Program",
			JOptionPane.QUESTION_MESSAGE);

			// Make connection and initialize streams
			Socket socket = new Socket(serverAddress, 10096);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			String response = null;
			
			out.println(clientName);
			serverName = in.readLine();
			Game.setServerName(serverName);
			Game.setClientName(clientName);
		

	}
	
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.connectToServer();

	}

}
