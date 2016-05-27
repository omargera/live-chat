package com.livechat.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.livechat.adapter.liveperson.LPChatSession;

/**
 * all interface functions are public
 */
public interface ChatSession {

	void sendMessage(String line) throws IllegalStateException,
			IOException, ChatApiException;

	List<Skill> getTransferSkills()
			throws IllegalStateException, IOException, ChatApiException;

	void transferToSkill(String skillID, String text)
			throws IOException, ChatApiException;
	
	void endChat() throws IOException, ChatApiException;
	
	public Map<String, String> getChatCustomVariables() throws ChatApiException, IOException;
	
	public String getChatUniqueIdentifier();
	
    public static class Skill{
        public String id;
        public String name;
    }

    enum ChatState {
            NOT_CONNECTED, REQUESTED, STARTED, ENDED, ERROR , TRANSFERED
    }

	void setChatListener(ChatListener listener);
}