// louise mamou 312111610 

package com.livechat.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;



public class Square extends JButton {

	private Point point;
	private Board board;
	public boolean realEnable;
	
	private static final long serialVersionUID = 1L;

	public Square(final Board board, int x, int y) {
		super();
		realEnable= true;
		point= new Point(x,y);
		this.board = board;
//		addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e)
//            {
//            	if (board.getGameMode() == GameMode.PARTUAL)
//            		board.addTool(point);
//            	else 
//            		board.updatePress(point);
//            	
//            }
//        }); 
		
	}

	public Board getBoard() {
		return board;
	}

	
}
