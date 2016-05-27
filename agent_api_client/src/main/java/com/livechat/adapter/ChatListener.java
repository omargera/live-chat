package com.livechat.adapter;

import java.util.List;

import com.livechat.adapter.ChatSession.ChatState;

public interface ChatListener {
	
	void chatEventRecieved(List<ChatMessage> chatMessage);
}
