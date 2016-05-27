package com.livechat.adapter;

import java.io.IOException;

public interface AgentManager {
	
	String test();
	
	void connect(String appKey);

	boolean login(String account, String username, String password);

	void setListener(AgentListener agentListener);
	
	ChatSession takeChat(ChatListener listener) throws ChatApiException, IOException;
}
