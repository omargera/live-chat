package com.livechat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;

public class Login {

	private JFrame frame;
	private JTextField txtUsername;
	private JTextField txtSiteid;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblSiteid = new JLabel("SiteId:");
		lblSiteid.setBounds(32, 17, 61, 16);
		panel.add(lblSiteid);
		
		txtSiteid = new JTextField();
		txtSiteid.setBounds(131, 6, 148, 39);
		panel.add(txtSiteid);
		txtSiteid.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(32, 68, 87, 16);
		panel.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(131, 57, 148, 39);
		panel.add(txtUsername);
		txtUsername.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String password = new String(passwordField.getPassword());
				String siteId = txtSiteid.getText();
				String username = txtUsername.getText();
				System.out.println("Login button clicked, Credentials are: site: " + siteId + " username: " + username + " password: " + password);
				
				
			}
		});
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(32, 119, 87, 16);
		panel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(131, 108, 148, 39);
		panel.add(passwordField);
		btnSubmit.setBounds(157, 189, 98, 39);
		panel.add(btnSubmit);
	}
}
