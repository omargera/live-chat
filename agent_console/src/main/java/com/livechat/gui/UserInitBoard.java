// louise mamou 312111610 

package com.livechat.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class UserInitBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private boolean whiteKingChosen, blackKingChosen;
	private int whiteSoldiers, blackSoldiers, whiteTools, blackTools;
	@SuppressWarnings("unused")
	private Game game;
	private MyButton wSold,wHorse,wTurret,wRunner,wQueen,wKing,bSold,bHorse,bTurret,bRunner,bQueen,bKing, play;
	
	public UserInitBoard(final Game game) {
		super( new GridLayout(2,7));
		this.game = game;
		whiteKingChosen= blackKingChosen =false;
		whiteSoldiers= blackSoldiers= whiteTools= blackTools= 0;
		
		wSold = new MyButton(new ImageIcon("soldierw.PNG"));
		wHorse = new MyButton(new ImageIcon("horsew.PNG"));
		wTurret = new MyButton(new ImageIcon("turretw.PNG"));
		wRunner = new MyButton(new ImageIcon("runnerw.PNG"));
		wQueen = new MyButton(new ImageIcon("queenw.PNG"));
		wKing = new MyButton(new ImageIcon("kingw.PNG"));
		bSold = new MyButton(new ImageIcon("soldierb.PNG"));
		bHorse = new MyButton(new ImageIcon("horseb.PNG"));
		bTurret = new MyButton(new ImageIcon("turretb.PNG"));
		bRunner = new MyButton(new ImageIcon("runnerb.PNG"));
		bQueen = new MyButton(new ImageIcon("queenb.PNG"));
		bKing = new MyButton(new ImageIcon("kingb.PNG"));
		play = new MyButton("play");
		play.setEnabled(false);
		play.realEnable = false;
		setEnable(Color.WHITE, false);
		setEnable(Color.BLACK, false);
		wKing.setEnabled(true);
		bKing.setEnabled(true);
		wKing.realEnable = true;
		bKing.realEnable = true;
		
		wSold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if ((++whiteTools) == 16) 
            			setEnable(Color.WHITE, false); 
            	else if ((++whiteSoldiers) == 8){
            		wSold.setEnabled(false);
            		wSold.realEnable= false;
            	}
//            	game.chooseLocation("soldier", Color.WHITE);
            	if ((++whiteTools) == 16 && blackTools == 16)
            			play.doClick();
            }});
		wHorse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               	if ((++whiteTools) == 16) 
        			setEnable(Color.WHITE, false); 
//            	game.chooseLocation("horse", Color.WHITE);
               	if ((++whiteTools) == 16 && blackTools == 16)
        			play.doClick();
            }});
		wTurret.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              	if ((++whiteTools) == 16) 
        			setEnable(Color.WHITE, false); 
//            	game.chooseLocation("turret", Color.WHITE);
              	if ((++whiteTools) == 16 && blackTools == 16)
        			play.doClick();
            }});
		wRunner.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if ((++whiteTools) == 16) 
        			setEnable(Color.WHITE, false); 
//            	game.chooseLocation("runner", Color.WHITE);
              	if ((++whiteTools) == 16 && blackTools == 16)
        			play.doClick();
            }});
		wQueen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if ((++whiteTools) == 16) 
        			setEnable(Color.WHITE, false); 
//            	game.chooseLocation("queen", Color.WHITE);
             	if ((++whiteTools) == 16 && blackTools == 16)
        			play.doClick();
            }});
		wKing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (blackKingChosen) {
            		play.setEnabled(true);
            		play.realEnable = true;
                   	setEnable(Color.WHITE, true);
                   	setEnable(Color.BLACK, true);
                 	bKing.setEnabled(false);
                	bKing.realEnable=false;
            	}
              	wKing.setEnabled(false);
            	wKing.realEnable=false;
            	whiteKingChosen=true;
//            	game.chooseLocation("king", Color.WHITE);
            }});
		
		bSold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               	if ((++blackTools) == 16) 
        			setEnable(Color.BLACK, false); 
               	else if ((++blackSoldiers) == 8){
               		bSold.setEnabled(false);
               		bSold.realEnable= false;
               	}
//            	game.chooseLocation("soldier", Color.BLACK);
            	if ((++blackTools) == 16 && whiteTools == 16)
        			play.doClick();
            }});
		bHorse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               	if ((++blackTools) == 16) 
            			setEnable(Color.BLACK, false); 
//            	game.chooseLocation("horse", Color.BLACK);
            	if ((++blackTools) == 16 && whiteTools == 16)
        			play.doClick();
            }});
		bTurret.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if ((++blackTools) == 16) 
        			setEnable(Color.BLACK, false);
//            	game.chooseLocation("turret", Color.BLACK);
               	if ((++blackTools) == 16 && whiteTools == 16)
            			play.doClick();
            }});
		bRunner.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              	if ((++blackTools) == 16) 
            			setEnable(Color.BLACK, false);
//            	game.chooseLocation("runner", Color.BLACK);
               	if ((++blackTools) == 16 && whiteTools == 16)
            			play.doClick();
            }});
		bQueen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              	if ((++blackTools) == 16) 
            			setEnable(Color.BLACK, false);
//            	game.chooseLocation("queen", Color.BLACK);
               	if ((++blackTools) == 16 && whiteTools == 16)
            			play.doClick();
            }});
		bKing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (whiteKingChosen){
            		play.setEnabled(true);
            		play.realEnable = true;
                  	setEnable(Color.WHITE, true);
                   	setEnable(Color.BLACK, true);
                	wKing.setEnabled(false);
                	wKing.realEnable=false;
            	}
            	bKing.setEnabled(false);
            	bKing.realEnable = false;
            	blackKingChosen=true;
//            	game.chooseLocation("king", Color.BLACK);
              
            }});
		
		play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	game.startGame();
            }});
		
		add(wSold);
		add(wHorse);
		add(wTurret);
		add(wRunner);
		add(wQueen);
		add(wKing);
		add(play);
		add(bSold);
		add(bHorse);
		add(bTurret);
		add(bRunner);
		add(bQueen);
		add(bKing);
	}
	
	public void setEnable(Color color, boolean b) {
		if (color == Color.BLACK) {
			bKing.setEnabled(b);
    		bSold.setEnabled(b);
    		bHorse.setEnabled(b);
    		bTurret.setEnabled(b);
    		bRunner.setEnabled(b);
    		bQueen.setEnabled(b);
    		bKing.realEnable = b;
    		bSold.realEnable = b;
    		bHorse.realEnable = b;
    		bTurret.realEnable = b;
    		bRunner.realEnable = b;
    		bQueen.realEnable = b;

		}
		else {
			wKing.setEnabled(b);
    		wSold.setEnabled(b);
    		wHorse.setEnabled(b);
    		wTurret.setEnabled(b);
    		wRunner.setEnabled(b);
    		wQueen.setEnabled(b);
    		wKing.realEnable = b;
    		wSold.realEnable = b;
    		wHorse.realEnable = b;
    		wTurret.realEnable = b;
    		wRunner.realEnable = b;
    		wQueen.realEnable = b;
		}
	}
}
