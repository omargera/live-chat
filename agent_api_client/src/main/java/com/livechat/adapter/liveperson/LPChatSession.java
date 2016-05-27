package com.livechat.adapter.liveperson;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.livechat.adapter.ChatApiException;
import com.livechat.adapter.ChatListener;
import com.livechat.adapter.ChatMessage;
import com.livechat.adapter.ChatSession;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class LPChatSession implements ChatSession {
	protected static final Logger log = Logger.getLogger(LPChatSession.class);

	String agentName;
	RequestHelper rh;
	String chatSessionURI;
	private Map<String, String> chatResources;
	List<ChatMessage> latestEvents;
	Map<String, String> latestInfo;
	private Map<String, String>	chatCustomVariables;

	ChatState chatState;
	private Thread chatThread;

	private ChatListener listener;

	public LPChatSession(RequestHelper rh, String location) {
		this.rh = rh;
		chatSessionURI = location;
		chatState = ChatState.STARTED;
	}

	/* (non-Javadoc)
	 * @see com.livechat.adapter.liveperson.ChatSession#getChatDetails()
	 */
	public void getChatDetails() throws IllegalStateException, IOException, ChatApiException {
		if (chatSessionURI == null) {
			throw new IllegalStateException("Not in chat state", null);
		}
		String req = chatResources != null ? chatResources.get("next") :  rh.addVersion(chatSessionURI);
		chatResources = null;
		byte[][] res = new byte[1][];
		int rc = rh.doGetRequest(req, res);
		if (rc == HttpStatus.SC_OK) {
			Node n = rh.getNode(res[0], "chat");
			if (n == null) {
				ChatApiException e = new ChatApiException(rc);
				e.message = "chat getInfo has no chat in the reply";
				throw e;
			}
			chatResources = new HashMap<String, String>();
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node item = nl.item(i);
				String name = item.getNodeName();
				String value = item.getTextContent();
				if (name.equals("link")) {
					String href = item.getAttributes().getNamedItem("href").getNodeValue();
					String rel = item.getAttributes().getNamedItem("rel").getNodeValue();

					chatResources.put(rel, rh.addVersion(href));
				}
				else if (name.equals("events")) {
					getEvents(item);
				}
				else if (name.equals("info")){
					getInfo(item);
				}
			}
			if (chatResources.size() == 0){
				int k=5;
			}
			this.chatCustomVariables = fetchChatCustomVariables();
			return;
		}

		chatResources = null;
		chatState = ChatState.ERROR;
		throw rh.buildErrorException(res[0], rc);
	}
	/* (non-Javadoc)
	 * @see com.livechat.adapter.liveperson.ChatSession#getInfo(org.w3c.dom.Node)
	 */
	public void getInfo(Node elements) {
		NodeList nl = elements.getChildNodes();
		latestInfo = new HashMap<String,String>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			String name = item.getNodeName();
			String value = item.getTextContent();
			if ("link".equals(name)) {
				String href = item.getAttributes().getNamedItem("href").getNodeValue();
				String rel = item.getAttributes().getNamedItem("rel").getNodeValue();
				chatResources.put(rel+"Info", rh.addVersion(href));
			}
			else {
				if ("state".equals(name)) {
					if ("chatting".equals(value)) {
						chatState = ChatState.STARTED;
					}
					else if ("ended".equals(value)) {
						chatState = ChatState.ENDED;
					}
				} else if ("agentName".equals(name)) {
					agentName = value;
				}
				latestInfo.put(name, value);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.livechat.adapter.liveperson.ChatSession#getEvents(org.w3c.dom.Node)
	 */
	public void getEvents(Node elements) {
		NodeList nl = elements.getChildNodes();
		latestEvents = new ArrayList<ChatMessage>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			String name = item.getNodeName();
			String value = item.getTextContent();
			if ("link".equals(item.getNodeName())) {
				String href = item.getAttributes().getNamedItem("href").getNodeValue();
				String rel = item.getAttributes().getNamedItem("rel").getNodeValue();
				chatResources.put(rel+"Events",  rh.addVersion(href));
			} else if ("event".equals(item.getNodeName())) {
				String type = item.getAttributes().getNamedItem("type").getNodeValue();
				String id = item.getAttributes().getNamedItem("id").getNodeValue();
				if ("line".equals(type)) {
					List<Node> retl = rh.getChildNode(item, "source");
					String source = retl.get(0).getTextContent();
					retl = rh.getChildNode(item, "text");
					String msg = retl.get(0).getTextContent();
					int systemID = -1;
					if ("agent".equals(source)) {
						retl = rh.getChildNode(item, "by");
						agentName = retl.get(0).getTextContent();
					}
					else if ("system".equals(source)) {
						retl = rh.getChildNode(item, "systemMessageId");
						String s = retl.get(0).getTextContent();
						systemID = Integer.valueOf(s);
					}

					ChatMessage m = new ChatMessage(agentName, "", msg, ChatMessage.MessageType.LINE, source, Integer.valueOf(id),systemID);
					latestEvents.add(m);
				} else if ("state".equals(type)) {
					List<Node> retl = rh.getChildNode(item, "state");
					String state = retl.get(0).getTextContent();
					if ("chatting".equals(state)) {
						chatState = ChatState.STARTED;
					} else if ("ended".equals(state)) {
						chatThread.interrupt();
						chatState = ChatState.ENDED;
					} else if ("transfered".equals(state)) {
						chatThread.interrupt();
						chatState = ChatState.TRANSFERED;
					} 

					ChatMessage m = new ChatMessage(agentName, "", state, ChatMessage.MessageType.STATUS, "status", Integer.valueOf(id));
					latestEvents.add(m);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void endChat() throws IOException,ChatApiException{
		int rc;

		String req=chatResources.get("events");
		byte[][]res=new byte[1][];
		String body="<event type=\"state\"><state>ended</state></event>";
		rc=rh.doPostRequest(req,body,res);
		if(rc==HttpStatus.SC_CREATED){
			return;
		}
		throw rh.buildErrorException(res[0],rc);
	}

	/* (non-Javadoc)
	 * @see com.livechat.adapter.liveperson.ChatSession#sendMessage(java.lang.String)
	 */
	public void sendMessage(String line)throws IllegalStateException,IOException,ChatApiException{
		if(chatState != ChatState.STARTED){
			throw new IllegalStateException("Not in chat state",null);
		}
		int rc;

		String req=chatResources.get("events");
		byte[][]res=new byte[1][];
		String body="<event type=\"line\"><text>"+line+"</text></event>";
		rc=rh.doPostRequest(req,body,res);
		if(rc==HttpStatus.SC_CREATED){
			return;
		}
		throw rh.buildErrorException(res[0],rc);
	}

	/* (non-Javadoc)
	 * @see com.livechat.adapter.liveperson.ChatSession#getTransferSkills()
	 */
	public List<Skill> getTransferSkills() throws IllegalStateException,IOException,ChatApiException{
		if (chatSessionURI == null) {
			throw new IllegalStateException("Not in chat state", null);
		}
		List<Skill> ret = new ArrayList<Skill>();
		while( chatResources == null){
			try {
				System.out.println("getTransferSkills >> chatResources is null retrying in 1000ms.");
				Thread.sleep(1000);
			} catch(InterruptedException ex) {
				System.out.println("getTransferSkills Error");
			}
		}
		String req = chatResources.get("transfer");
		byte[][] res = new byte[1][];
		int rc = rh.doGetRequest(req, res);
		if (rc == HttpStatus.SC_OK) {
			Node n = rh.getNode(res[0], "transfer");
			if (n == null) {
				ChatApiException e = new ChatApiException(rc);
				e.message = "chat getTransferSkills has no transfer in the reply";
				throw e;
			}
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node item = nl.item(i);
				String name = item.getNodeName();
				if (name.equals("skill")) {
					NodeList nl1 = item.getChildNodes();
					Skill s = new Skill();
					for (int j = 0; j < nl1.getLength(); j++) {
						item = nl1.item(j);
						name = item.getNodeName();
						String value = item.getTextContent();
						if (name.equals("id")){
							s.id = value;
						}
						else if (name.equals("name")){
							s.name = value;
						}
					}
					ret.add(s);
				}
			}
			return ret;
		}
		throw rh.buildErrorException(res[0], rc);

	}
	/* (non-Javadoc)
	 * @see com.livechat.adapter.liveperson.ChatSession#transferToSkill(java.lang.String, java.lang.String)
	 */
	public void transferToSkill(String skillID, String text)throws IOException, ChatApiException{
		String body = "<transfer><skill><id>"+skillID+"</id></skill><text>"+text+"</text></transfer>";
		byte[][] res = new byte[1][];
		int rc = rh.doPostRequest(chatResources.get("transfer"),body, res);
		if (rc == HttpStatus.SC_OK) {
			chatState = ChatState.TRANSFERED;
			return;
		}
		throw rh.buildErrorException(res[0], rc);
	}

	private void poolNewChatEvents(){

		this.chatThread = new Thread(){
			public void run(){
				log.info("started pooling for new chat events");
				try {
					while (!this.isInterrupted()){
						doPoolNewChatEvents();

						try {
							Thread.sleep(3000);
						}
						catch (InterruptedException e) {
							log.error("Error", e);
						}
					}
				} catch (IllegalStateException | IOException | ChatApiException e) {
					log.error(e);
				}
			}
		};

		chatThread.start();
	}

	private void doPoolNewChatEvents() throws IllegalStateException, IOException, ChatApiException {
		getChatDetails();
		if (this.latestEvents.size() != 0){
			if (this.listener != null){
				this.listener.chatEventRecieved(this.latestEvents);
			}
		}
	}

	@Override
	public void setChatListener(ChatListener listener) {
		this.listener = listener;
		poolNewChatEvents();
	}

	public Map<String,String> fetchChatCustomVariables() throws ChatApiException, IOException {
		String req = chatResources.get("visit-id");
		byte[][] res = new byte[1][];
		int rc = rh.doGetRequest(req, res);
		if (rc == HttpStatus.SC_OK) {
			Node n = rh.getNode(res[0], "visitId");
			if (n == null) {
				ChatApiException e = new ChatApiException(rc);
				e.message = "chat getInfo has no chat in the reply";
				throw e;
			}
			//			chatResources = new HashMap<String, String>();
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node item = nl.item(i);
				String name = item.getNodeName();
				String value = item.getTextContent();
				if (name.equals("link")) {
					String href = item.getAttributes().getNamedItem("href").getNodeValue();
					String rel = item.getAttributes().getNamedItem("rel").getNodeValue();
					if (rel.equals("custom-variables")){
						return getCustomVariablesList(href);
					}
					chatResources.put(rel, rh.addVersion(href));
				}
			}
		}
		return null;
	}

	private Map<String, String> getCustomVariablesList(String req) throws IOException, ChatApiException {
		byte[][] res = new byte[1][];
		int rc = rh.doGetRequest(rh.addVersion(req), res);
		HashMap<String,String> result = new LinkedHashMap<>();
		if (rc == HttpStatus.SC_OK) {
			Node n = rh.getNode(res[0], "customVariables");
			if (n == null) {
				ChatApiException e = new ChatApiException(rc);
				e.message = "chat getInfo has no chat in the reply";
				throw e;
			}
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node item = nl.item(i);
				String name = item.getNodeName();
				if (name.equals("customVariable")) {
					NodeList customVarDetailNodeList = item.getChildNodes();
					String customVarName = null;
					String customVarValue = null;
					for (int j = 0; j < customVarDetailNodeList.getLength(); j++) {
						Node customVarDetailItem = customVarDetailNodeList.item(j);
						String customVarDetailItemName = customVarDetailItem.getNodeName();
						String customVarDetailItemValue = customVarDetailItem.getTextContent();
						if (customVarDetailItemName.equals("name")){
							customVarName = customVarDetailItemValue;
						} else if (customVarDetailItemName.equals("value")){
							customVarValue = customVarDetailItemValue;
						}
					}
					result.put(customVarName, customVarValue);

				}
			}
		}
		return result;
	}

	public Map<String, String> getChatCustomVariables() {
		return chatCustomVariables;
	}

	@Override
	public String getChatUniqueIdentifier() {
		return chatSessionURI;
	}
}
