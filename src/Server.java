import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Server {
	private Home homeFrame;
	private static int serverTime;
	private static int serverTime2;

	private static int clientTime;
	private static int clientTime2;
	
	private static int serverScore=0;	
	private static int clientScore=0;
	
	private static String serverName;
	private static String clientName;
	private static String currentPlayer;
	public Server() throws NumberFormatException, ScriptException{
		serverName = JOptionPane.showInputDialog("Please enter you name");
		homeFrame = Home.createAndShowGUI();
	}
	
	public static void main(String[] args) throws Exception {
		Server server = new Server();

		System.out.println("Rainy Word is running.");
		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(10096);
		try {
			while (true) {
				new Capitalizer(listener.accept(), clientNumber++).start();

			}
		} finally {
			listener.close();
		}
		
	}
	private static class Capitalizer extends Thread {
		private Socket socket;
		private int clientNumber;

		public Capitalizer(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			log("New connection with client# " + clientNumber + " at " + socket);
		}
		
		
		private void log(String message) {
			System.out.println(message);
		}
		public void run() {

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				out.println(serverName);
				clientName = in.readLine();
				Game.setServerName(serverName);
				Game.setClientName(clientName);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
}
