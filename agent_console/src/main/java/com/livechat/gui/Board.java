// louise mamou 312111610 

package com.livechat.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Board extends JPanel{

	private static Color myBlue= new Color(173,216,230), myGray = new Color(190,190,190),
							myPink = new Color(255,153,153);
	private static final long serialVersionUID = 1L;
	private Game game;
	public Square[][] squares;
	
	public Board(Game game) {
		super(new GridLayout(8,8)); 
		this.game=game;
		squares = new Square[8][8];
		
		for(int i=0; i<8; ++i) 
			for(int j=0; j<8; ++j) {
				
				final Square button= new Square(this, i,j);
				squares[i][j] = button;
				if (i%2 == j%2)
					button.setBackground(myBlue);
				else 
					button.setBackground(myGray);
				this.add(button);
			}
		
	}
	
//	public void updatePress(Point point) {
//		if ((game.getLast() == null) && (game.firstPressLegal(point))) {
//			game.setLast(point); 
//			int x= (int) point.getX();
//			int y= (int) point.getY();
//			squares[x][y].setBackground(myPink);
//		}
//		
//		else if (game.getLast()!=null) {
//			game.secondPress(point);
//			int x= (int) game.getLast().getX();
//			int y= (int) game.getLast().getY();
//			if (x%2 == y%2)
//				squares[x][y].setBackground(myBlue);
//			else 
//				squares[x][y].setBackground(myGray);
//			game.setLast(null);
//
//		}
//	}
	
	public void setIcon(Point point, String type) {
		
		int x= (int) point.getX();
		int y= (int) point.getY();
		type = type + ".PNG";
		squares[x][y].setIcon(new ImageIcon(type));
		
	}
	
	public void setBoard(String[][] typeArray) { 
		
		Icon icon;
		for(int i=0; i<8; ++i)
			for (int j=0; j<8; ++j) {
				icon = new ImageIcon(typeArray[i][j]+".PNG");
				squares[i][j].setIcon(icon);
			}
	}
	
//	public GameMode getGameMode() {
//		return game.getGameMode();
//	}
//	
//	public void addTool(Point location) {
//		game.addTool(location);
//	}
	
	public void enableSquare(Point location,boolean b) {
		int x= (int) location.getX();
		int y= (int) location.getY();
		squares[x][y].setEnabled(b);
		squares[x][y].realEnable = b;
	}
}
