package com.livechat;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JFrame;

import com.livechat.adapter.AgentManager;
import com.livechat.adapter.liveperson.LPAgentManagerImpl;

public class Application {

private static AgentManager agentManager;
private static JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					init();
					
					LoginView window = new LoginView(frame, agentManager);
					
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void init() {
		String appKey = "0a1e11a815f54ceeb5a217fe6e6a9676";
		String appSecret = "";
		String tokenKey= "";
		String tokenSecret = "";

		agentManager = new LPAgentManagerImpl();
		agentManager.connect(appKey);
		

		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
	}

}
