
// louise mamou 312111610 
package com.livechat.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class Menu extends JPanel{

	private static final long serialVersionUID = 1L;
	private Game game;
	
	public Menu(final Game game) {
		super(new FlowLayout()); 
		this.setGame(game);
		JButton twoPlayers= new JButton("Two Players");
		JButton onePlayer= new JButton("One Player");
		JButton random= new JButton("Randomal Game");
		JButton partual= new JButton("Partual Game");


		add(twoPlayers);
		add(onePlayer);
		add(random);
		add(partual);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}