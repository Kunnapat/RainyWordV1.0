import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GameClient extends JFrame{
	static int windowWidth = 1000;
	static int windowHeight = 700;
	JPanel gamePanel, optionPanel, infoPanel;
	static JLabel serverNameLabel;
	static JLabel clientNameLabel;
	JFrame popUpFrame;
	static LinkedList wordList = new LinkedList();
	LinkedList welcomeList = new LinkedList();
    String[] color = {"red", "black", "white","grey","green","yellow","orange","purple","pink"};
    String inputWord;
	static String serverName;
	static String clientName;
    static JTextField inputField;
    static JButton startButton;
    boolean gameStarted = false;
    Thread t1;
    int score = 0;
    static BufferedReader in;
    static PrintWriter out;
    
	public GameClient(){
		super("Rainy Word V1.1");
		createGamePanel();
		createOptionPanel();
		createInfoPanel();
		this.add(infoPanel, BorderLayout.NORTH);
		this.add(gamePanel,BorderLayout.CENTER);
		this.add(optionPanel,BorderLayout.SOUTH);
		
	}

	private void createInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setSize(new Dimension(1000,300));
		infoPanel.setBackground(Color.GRAY);
		infoPanel.add(serverNameLabel);
		infoPanel.add(clientNameLabel);
		
	}
	private void createGamePanel() {
		gamePanel = new GamePanel();
		gamePanel.setSize(new Dimension(1000,300));
		gamePanel.setBackground(Color.BLACK);
//		LinkedListItr itr1 = wordList.zeroth();
		LinkedListItr itr2 = welcomeList.zeroth();
//		int temp = 5;
//		for(int i = 0; i < color.length; i++){
//			wordList.insert(new Word(temp*-200,color[i]), itr1);
//			temp++;
//		}
		welcomeList.insert(new Word(170,-150,"-------------------"+serverName+" has started the game"+"-------------------"), itr2);
		welcomeList.insert(new Word(170,170,"▒█▀▀█ ░█▀▀█ ▀█▀ ▒█▄░▒█ ▒█░░▒█ 　 ▒█░░▒█ ▒█▀▀▀█ ▒█▀▀█ ▒█▀▀▄"), itr2);
		welcomeList.insert(new Word(170,190,"▒█▄▄▀ ▒█▄▄█ ▒█░ ▒█▒█▒█ ▒█▄▄▄█ 　 ▒█▒█▒█ ▒█░░▒█ ▒█▄▄▀ ▒█░▒█"), itr2);
		welcomeList.insert(new Word(170,210,"▒█░▒█ ▒█░▒█ ▄█▄ ▒█░░▀█ ░░▒█░░ 　 ▒█▄▀▄█ ▒█▄▄▄█ ▒█░▒█ ▒█▄▄▀"), itr2);
		welcomeList.insert(new Word(170,320,"---------------------------------------------------------"), itr2);
		
		repaint();
	}
	
	private void createOptionPanel() {
		optionPanel = new JPanel();
		optionPanel.setSize(new Dimension(1000,200));
		optionPanel.setBackground(Color.GRAY);
		startButton = new JButton("READY");
		startButton.setEnabled(false);
		
		
		t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(gameStarted){
					
					repaint();
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(wordList.isEmpty()){
						createPopUpFrame();
						gameStarted = false;
						repaint();
						popUpFrame.setVisible(true);
					}
				}
				
			}
			
		});
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				out.println("client ready");
				startButton.setEnabled(false);
				inputField.setText("");
				inputField.setEnabled(true);
				t1.start();
				gameStarted = true;
				
			}
			
			
		});
		optionPanel.add(startButton);
		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension(500,20));
		inputField.setBackground(Color.white);
		inputField.setForeground(Color.black);
		inputField.setFont(new Font("Menlo",Font.PLAIN,12));
		inputField.setText("Waiting for " + serverName + " to start the game");
		inputField.setEnabled(false);
		inputField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				inputWord = inputField.getText().toLowerCase();
				inputField.setText("");
				System.out.println(inputWord);
				LinkedListItr itr1 = wordList.first();
				while(!itr1.isPastEnd()){
					String temp = itr1.current.element.word.toLowerCase();
					if(inputWord.equals(temp)){
						playSound("src/correct.wav");
						System.out.println("correct");
						wordList.remove(itr1);
						score = score + 1;
						break;
					}
					itr1.advance();
					if(itr1.isPastEnd()){
						playSound("src/wrong.wav");
					}
				}
				
			}
			
		});
		optionPanel.add(inputField);
	
		
	}

	public void playSound(String s) {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}

	private void createPopUpFrame() {
		popUpFrame = new JFrame();
		popUpFrame.setSize(400, 150);
		popUpFrame.setResizable(false);
		popUpFrame.setLocationRelativeTo(null);
		popUpFrame.setLayout(new GridLayout(3, 1));
		JLabel winLabel = new JLabel("Your score is " + score + "/" + color.length +".");
		popUpFrame.add(winLabel);
		JButton closeButton = new JButton("OK");
		closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		closeButton.setSize(new Dimension(80,40));
		popUpFrame.add(closeButton);
		popUpFrame.setVisible(false);
		
	}
	
	
	
	public static GameClient createAndShowGUI(LinkedList ll) throws UnknownHostException, IOException{
		GameClient frame = new GameClient();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        wordList = ll;
        Socket socket =new Socket("localhost",3333);  
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(),true);
		Thread connection = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					try {
						doCommand(in.readLine());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}

			private void doCommand(String s) {
				if(s.equals("server ready")){
					startButton.setEnabled(true);
					inputField.setText(serverName+" is ready. Are you ready?");
				}else{
					
				}
				
			}
			
		});
		connection.start();
        return frame;
	}


	class GamePanel extends JPanel{
		boolean firstTime = true;
		
		public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			if(firstTime){
				g2.setColor(Color.green);
				g2.setFont(new Font("Menlo",Font.PLAIN,20));
				g2.drawString("▒█▀▀█ ░█▀▀█ ▀█▀ ▒█▄░▒█ ▒█░░▒█ 　 ▒█░░▒█ ▒█▀▀▀█ ▒█▀▀█ ▒█▀▀▄ ", 170, 170);
				g2.drawString("▒█▄▄▀ ▒█▄▄█ ▒█░ ▒█▒█▒█ ▒█▄▄▄█ 　 ▒█▒█▒█ ▒█░░▒█ ▒█▄▄▀ ▒█░▒█ ", 170, 190);
				g2.drawString("▒█░▒█ ▒█░▒█ ▄█▄ ▒█░░▀█ ░░▒█░░ 　 ▒█▄▀▄█ ▒█▄▄▄█ ▒█░▒█ ▒█▄▄▀  ", 170, 210);
				g2.drawString("---------------------------------------------------------", 170, 320);
				
				firstTime = false;
			}else{
				g2.setColor(Color.BLACK);
				g2.drawRect(0, 0, windowWidth, windowHeight);
		        LinkedListItr itr1 = wordList.first();
		        LinkedListItr itr2 = welcomeList.first();
		        while(!itr2.isPastEnd()){
		        	itr2.current.element.paint(g2);
		        	itr2.advance();
		        }
		        while(!itr1.isPastEnd()){
		        	itr1.current.element.paint(g2);
		        	itr1.advance();
		        }
		        update();
			}
				
		}
		


		public void update(){
			LinkedListItr itr1 = wordList.first();
			LinkedListItr itr2 = welcomeList.first();
	        while(!itr1.isPastEnd()){
	        	itr1.current.element.update();
	        	if(itr1.current.element.getYLocation() > windowHeight){
	        		wordList.remove(itr1);
	        		playSound("src/wrong.wav");
	        	}
	        	itr1.advance();
	        }
	        while(!itr2.isPastEnd()){
	        	itr2.current.element.update();
	        	if(itr2.current.element.getYLocation() > windowHeight){
	        		wordList.remove(itr2);
	        	}
	        	itr2.advance();
	        }
		}
	}

	
	public static void setServerName(String s){
		if(s == null){
			s = "";
		}
		serverName = s;
		serverNameLabel = new JLabel("Server Name: " + serverName);	
		serverNameLabel.setForeground(Color.WHITE);
	}
	
	public static void setClientName(String s){
		if(s == null){
			s = "";
		}
		clientName = s;
		clientNameLabel = new JLabel("Client Name: " + clientName);
		clientNameLabel.setForeground(Color.WHITE);
	}
	
	

}