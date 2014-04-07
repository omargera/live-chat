// louise mamou 312111610 

package com.livechat.gui;

import javax.swing.*;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Game implements Observer 
{
	private JFrame frame;
	private Menu menu;
	private Board board;
	JLabel title;
	
	//normal game arguments
	private Color whoseTurn;
	private Point last;
//	private ChooseNewTool chooseTool;
	
	//partual game arguments
	private String typeToAdd;
	private Color colorToAdd;
	private UserInitBoard initBoard;
	
	private TextField siteId;
	private TextField username;
	private TextField password;
	private Button submit; 

	
	public Game()
	{
		whoseTurn = Color.WHITE;
		last=null;
		frame = new JFrame("Live Chat");
		initBoard = new UserInitBoard(this);
		
		siteId = new TextField("Enter you account id");
		username = new TextField("Enter your username");
		password = new TextField("Enter your password");
		submit = new Button("submit");
		
		frame.setLayout(new BorderLayout());
		frame.setSize(700,700);
		
		title= new JLabel("Chat Agent Client");
		frame.add(title, BorderLayout.NORTH);
		frame.add(new JLabel("    "), BorderLayout.WEST);
		frame.add(new JLabel("    "), BorderLayout.EAST);
		frame.add(new JLabel("    "), BorderLayout.SOUTH);
		frame.add(siteId);
		frame.add(username);
		frame.add(password);
		frame.add(submit);
		frame.setVisible(true);
	}
	
	public void twoPlayersGame() {
		menu.setVisible(false);
		board.setVisible(true);
		frame.add(board, BorderLayout.CENTER);		
		title.setText("   white turn!");
	}
	
	public void startGame() {
		initBoard.setVisible(false);
		title.setVisible(true);
		frame.add(title, BorderLayout.NORTH);
		for (int i=0; i<8; ++i)
			for(int j=0; j<8; ++j)
				board.enableSquare(new Point(i,j),true);
		title.setText("   white turn!");

	}
	
	public Point getLast() {
		return last;
	}

	public void setLast(Point last) {
		this.last = last;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}