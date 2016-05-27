package com.livechat.adapter.liveperson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.livechat.adapter.AgentListener;
import com.livechat.adapter.AgentManager;
import com.livechat.adapter.ChatApiException;
import com.livechat.adapter.ChatListener;
import com.livechat.adapter.ChatSession;

public class LPAgentManagerImpl implements AgentManager {

	protected static final Logger log = Logger.getLogger(LPAgentManagerImpl.class);

	private String uri;
	private RequestHelper rh;
	private AgentSessionHelper agent;
	private AgentListener agentListener;
	private List<ChatListener> chatListenerList;

	public String test(){
		return "Test function works";
	}

	public void connect(String appKey){

		RequestHelper rh = new RequestHelper(appKey);

		uri = "https://api.liveperson.net";

		agent = new AgentSessionHelper(rh, uri);

		log.info("Application Started");

	}

	public boolean login(String account, String username, String password){
		try {
			agent.login(account, username, password);

			poolAvailableChats();
			log.info("Successfuly logged in with the account: " + account + " and username: " + username);

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void poolAvailableChats(){
		
		Thread thread = new Thread(){
			public void run(){
				log.info("started pooling for available chat");

				doPoolAvailableChats();
			}
		};

		thread.start();

	}

	private void doPoolAvailableChats() {
		int oldNumberOfAvailableChats = 0;
		
		while (true) {
			try {
				int ringing = agent.getRingingCount();
				if (ringing != oldNumberOfAvailableChats) {
					if (agentListener != null){
						agentListener.numberOfChatsChanged(ringing);
					}
					oldNumberOfAvailableChats = ringing;
				}

				try {
					Thread.sleep(3000);
				}
				catch (InterruptedException e) {
					log.error("Error", e);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setListener(AgentListener agentListener) {
		if (agentListener != null){
			this.agentListener = agentListener;
		}
	}

	@Override
	public 	ChatSession takeChat(ChatListener listener) throws ChatApiException, IOException{
		LPChatSession chatSession = agent.takeChat();
		chatSession.getChatDetails();
		chatSession.setChatListener(listener);
		return chatSession;
	}
}