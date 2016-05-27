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

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import com.livechat.adapter.AgentManager;
import com.livechat.adapter.liveperson.LPAgentManagerImpl;

import java.awt.Font;

public class LoginView {

	private JFrame frame;
	private JTextField txtUsername;
	private JTextField txtSiteid;
	private JPasswordField passwordField;

	private AgentManager agentManager;

	/**
	 * Create the application.
	 */
	public LoginView(JFrame frame, com.livechat.adapter.AgentManager agentManager) {
		this.frame = frame;
		this.agentManager = agentManager;
		
		initializeUI();
		initializeAgentAPIClient();
	}

	/**
	 * Initialize the agent API client.
	 */
	private void initializeAgentAPIClient() {

		String appKey = "0a1e11a815f54ceeb5a217fe6e6a9676";
		String appSecret = "3d60ad41f2fb3750";
		String tokenKey= "b0a615fb41b848f582974bb65497c08f";
		agentManager = new LPAgentManagerImpl();
		agentManager.connect(appKey);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeUI() {

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		frame.setBounds(frame.getBounds().x, frame.getBounds().y, 350, 400);
		//frame.setResizable(false);
		panel.setLayout(null);

		JLabel lblSiteid = new JLabel("SiteId:");
		lblSiteid.setBounds(32, 77, 61, 16);
		panel.add(lblSiteid);

		txtSiteid = new JTextField();
		txtSiteid.setEditable(false);
		txtSiteid.setText("P33119011");
		txtSiteid.setBounds(131, 66, 148, 39);
		panel.add(txtSiteid);
		txtSiteid.setColumns(10);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(32, 128, 87, 16);
		panel.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setText("omar.gera");
		txtUsername.setBounds(131, 117, 148, 39);
		panel.add(txtUsername);
		txtUsername.setColumns(10);
		
		final JLabel lblLoginFailedPlease = new JLabel("Login Failed. Please try again!");
		lblLoginFailedPlease.setForeground(Color.RED);
		lblLoginFailedPlease.setBounds(131, 268, 180, 14);
		lblLoginFailedPlease.setVisible(false);
		panel.add(lblLoginFailedPlease);
		

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String password = new String(passwordField.getPassword());
				String siteId = txtSiteid.getText();
				String username = txtUsername.getText();
				System.out.println("Login button clicked, Credentials are: site: " + siteId + " username: " + username + " password: " + password);
				System.out.println("Trying to login...");
				if (agentManager.login(siteId, username, password) == true) {
					System.out.println("Logged in successfuly!");
					
					frame.getContentPane().removeAll();;
					frame.setVisible(false);
					
					ChatView window = new ChatView(frame, agentManager);
					
					frame.setVisible(true);

					
				} else {
					System.out.println("Login Failed, Please try again!");
					lblLoginFailedPlease.setVisible(true);
					
				}
			}
		});

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(32, 179, 87, 16);
		panel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(131, 168, 148, 39);
		panel.add(passwordField);
		btnSubmit.setBounds(131, 218, 148, 39);
		panel.add(btnSubmit);
		
		JLabel lblNewLabel = new JLabel("");
		//This needs to be changed
		lblNewLabel.setIcon(new ImageIcon("img3.png"));
		lblNewLabel.setBounds(19, 206, 100, 99);
		panel.add(lblNewLabel);
		
		JLabel lblCrmAgentLogin = new JLabel("CRM Agent Login");
		lblCrmAgentLogin.setFont(new Font("Lucida Sans", Font.BOLD, 20));
		lblCrmAgentLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrmAgentLogin.setBounds(32, 11, 241, 44);
		panel.add(lblCrmAgentLogin);
	}
}
