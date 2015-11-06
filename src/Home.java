import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Home extends JFrame{
	static int windowWidth = 500;
	static int windowHeight = 300;
    JButton startButton, gameSetting;
	public Home(){
		super("Connection");
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Game game = Game.createAndShowGUI();
				dispose();
			}
			
		});
		this.add(startButton,BorderLayout.NORTH);
		gameSetting = new JButton("Setting");
		this.add(gameSetting, BorderLayout.SOUTH);
		
	}
	

	public static Home createAndShowGUI(){
		Game.setServerName("Default");
		Game.setClientName("Default");
		Home frame = new Home();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        return frame;
	}
	public static void main(String[] args){
		createAndShowGUI();
	}


	
}