// louise mamou 312111610 

package com.livechat.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class MyButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean realEnable;
	
	public MyButton(ImageIcon icon) {
		super(icon);
		realEnable = true;
	}
	
	public MyButton(String str) {
		super(str);
		realEnable = true;
	}
}
