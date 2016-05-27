package com.livechat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JTextPane;

import com.livechat.adapter.AgentListener;
import com.livechat.adapter.AgentManager;
import com.livechat.adapter.ChatApiException;
import com.livechat.adapter.ChatListener;
import com.livechat.adapter.ChatMessage;
import com.livechat.adapter.ChatSession;
import com.livechat.adapter.ChatMessage.MessageType;
import com.livechat.adapter.ChatSession.Skill;

import javax.swing.SwingConstants;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JMenu;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

class ChatView implements AgentListener{

	private JFrame frame;

	private com.livechat.adapter.AgentManager agentManager;

	private ChatSession currentChatSession;
	
	private JLabel lblNumberOfChats;
	private JTextPane textPane;
	private JButton btnAcceptChat;
	private JButton btnSend;
	private JButton btnEndChat;
	private JMenu mnTransferMenu;
	
	class CurrentChatListener implements ChatListener{

		@Override
		public void chatEventRecieved(List<ChatMessage> chatList) {
			for (ChatMessage chatMessage : chatList){
				if (chatMessage.getType() == MessageType.STATUS){
					if (chatMessage.getMessage().equals("ended") || chatMessage.getMessage().equals("transfered")){
						textPane.setText(textPane.getText() + "\nThe user has left the Conversation");
						btnSend.setEnabled(false);
						btnEndChat.setEnabled(false);
						mnTransferMenu.setEnabled(false);
						textPane.setEnabled(false);
						currentChatSession = null;
					}
				}else{ // text message
					textPane.setText(textPane.getText() + "\n" + chatMessage.getSource() + ": " + chatMessage.getMessage());
				}
			}
		}

		/*@Override
		public void chatStatusChanged(ChatState state) {
			if (state == ChatState.ENDED || state == ChatState.TRANSFERED){
				textPane.setText(textPane.getText() + "\nThe user has left the Conversation");
				btnSend.setEnabled(false);
				btnEndChat.setEnabled(false);
				mnTransferMenu.setEnabled(false);
				textPane.setEnabled(false);
				currentChatSession = null;
			}
			
		}*/

	};
	
	private CurrentChatListener currentChatListener;

	/**
	 * Create the Chat View.
	 */
	public ChatView(JFrame frame, AgentManager agentManager) {
		this.agentManager = agentManager;
		this.frame = frame;
		agentManager.setListener(this);
		currentChatListener = new CurrentChatListener();
		initializeUI();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeUI() {
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		frame.setBounds(frame.getBounds().x, frame.getBounds().y, 545, 535);
		
		btnAcceptChat = new JButton("Accept chat");
		btnAcceptChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					currentChatSession = agentManager.takeChat(currentChatListener);
					
					btnSend.setEnabled(true);
					btnEndChat.setEnabled(true);
					populateSkillsMenu();
					
				} catch (ChatApiException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnAcceptChat.setBounds(214, 26, 91, 29);
		btnAcceptChat.setEnabled(false);
		panel.add(btnAcceptChat);
		
		btnEndChat = new JButton("End Chat");
		btnEndChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				try {
					currentChatSession.endChat();
				} catch (IOException | ChatApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnEndChat.setBounds(431, 26, 91, 29);
		btnEndChat.setEnabled(false);
		panel.add(btnEndChat);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(10, 67, 512, 345);
		panel.add(textPane);
		
		final JTextField inputField = new JTextField();
		inputField.setBounds(15, 423, 387, 67);
		panel.add(inputField);
		inputField.setColumns(10);
		
		inputField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\n'){
					sendChatMessage(inputField.getText());
					inputField.setText("");
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendChatMessage(inputField.getText());
				inputField.setText("");
			}
		});
		
		btnSend.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\n'){
					sendChatMessage(inputField.getText());
					inputField.setText("");
				}				
			}
		});
		btnSend.setBounds(412, 423, 110, 67);
		btnSend.setEnabled(false);
		panel.add(btnSend);
		
		JLabel lblNumberOfChatsLabel = new JLabel("Number of chats available:");
		lblNumberOfChatsLabel.setBounds(10, 31, 158, 16);
		panel.add(lblNumberOfChatsLabel);
		
		lblNumberOfChats = new JLabel("0");
		lblNumberOfChats.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumberOfChats.setBounds(143, 32, 61, 16);
		panel.add(lblNumberOfChats);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(320, 26, 91, 29);
		panel.add(menuBar);
		
		mnTransferMenu = new JMenu("  Transfer To  ");
		mnTransferMenu.setEnabled(false);
		menuBar.add(mnTransferMenu);
		mnTransferMenu.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	private void sendChatMessage(final String text) {
		try {
			currentChatSession.sendMessage(text);
		} catch (IllegalStateException | IOException | ChatApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void numberOfChatsChanged(int numberOfChats) {
		if ( numberOfChats > 0){
			btnAcceptChat.setEnabled(true);
		}else{ 
			btnAcceptChat.setEnabled(false);
		}
		lblNumberOfChats.setText(Integer.toString(numberOfChats));
	}

	private void populateSkillsMenu() throws IOException, ChatApiException {
		mnTransferMenu.removeAll();
		mnTransferMenu.setEnabled(true);
		//populate transfer menu
		for (final Skill skill : currentChatSession.getTransferSkills()){
			JMenuItem menuItem = createSkillMenuItem(skill);
			mnTransferMenu.add(menuItem);
		}
	}

	private JMenuItem createSkillMenuItem(final Skill skill) {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText(skill.name);			
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				transferChat(skill);
				
			}
		});
		return menuItem;
	}

	private void transferChat(Skill skill) {
		try {
			currentChatSession.transferToSkill(skill.id, skill.name);
		} catch (IOException | ChatApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
