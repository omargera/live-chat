package com.livechat.adapter.liveperson;

import org.apache.commons.httpclient.HttpStatus;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.livechat.adapter.ChatApiException;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class AgentSessionHelper {
    RequestHelper rh;
    final String uri;
    private String agentSessionRequest;
    private String agentSessionURI;
    private Map<String,String> agentResources;

    public AgentSessionHelper(RequestHelper rh, String uri) {
        this.rh = rh;
        this.uri = uri;
    }

    public void initRequests(String account) throws IOException, ChatApiException {
        String req = rh.addVersion(uri + "/api/account/" + account);
        byte[][] res = new byte[1][];
        int rc = rh.doGetRequest(req, res);
        if (rc == HttpStatus.SC_OK) {
            Node n = rh.getNode(res[0], "account");
            if (n == null) {
                return;
            }
            NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node item = nl.item(i);
                if ("link".equals(item.getNodeName())) {
                    String href = item.getAttributes().getNamedItem("href").getNodeValue();
                    String rel = item.getAttributes().getNamedItem("rel").getNodeValue();
                    if (rel.equals("agent-session")) {
                        agentSessionRequest = rh.addVersion(href);
                    }
                }
            }
            return;
        }
        throw rh.buildErrorException(res[0], rc);
    }

    public void login(String account, String user, String password) throws ChatApiException, IOException {
        initRequests(account);

        String body = "<loginData><userName>" + user + "</userName><password>"+password+"</password></loginData>";

        byte[][] res = new byte[1][];
        StringBuffer sb = new StringBuffer();
        int rc = rh.doPostRequest(agentSessionRequest,body, res, "Location", sb);
        if (rc == HttpStatus.SC_CREATED) {
            agentSessionURI = sb.toString();
            getAgentSessionLinks();
            setAvailability("Online");
            return;
        }
        throw rh.buildErrorException(res[0], rc);
    }

    public void setAvailability(String avail) throws ChatApiException, IOException {
        String body = "<availability><chat>" + avail + "</chat></availability>";

        byte[][] res = new byte[1][];
        int rc = rh.doPutRequest(agentResources.get("availability"),body, res);
        if (rc == HttpStatus.SC_OK) {
            return;
        }
        throw rh.buildErrorException(res[0], rc);
    }

    public int getRingingCount() throws IOException, ChatApiException {
        String req = agentResources.get("incoming-requests");
        byte[][] res = new byte[1][];
        int rc = rh.doGetRequest(req, res);
        if (rc == HttpStatus.SC_OK) {
            String st = new String(res[0]);
            Node n = rh.getNode(res[0], "incomingRequests");
            if (n == null) {
                ChatApiException e = new ChatApiException(rc);
                e.message = "getRingingCount has no incomingRequests in the reply";
                throw e;
            }
            int count = 0;
            NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node item = nl.item(i);
                if ("link".equals(item.getNodeName())) {
                    String href = item.getAttributes().getNamedItem("href").getNodeValue();
                    String rel = item.getAttributes().getNamedItem("rel").getNodeValue();
                    agentResources.put(rel,rh.addVersion(href));
                }
                else if ("ringingCount".equals(item.getNodeName())){
                    String val = item.getFirstChild().getTextContent();
                    count = val == null || val.equals("") ? 0 : Integer.parseInt(val);
                }
            }
            return count;
        }
        
        throw rh.buildErrorException(res[0], rc);
    }

    public LPChatSession takeChat() throws ChatApiException, IOException {
        byte[][] res = new byte[1][];
        StringBuffer sb = new StringBuffer();
        int rc = rh.doPostRequest(agentResources.get("incoming-requests"),"", res, "Location", sb);
        if (rc == HttpStatus.SC_CREATED) {
            return new LPChatSession(rh, sb.toString());
        }
        throw rh.buildErrorException(res[0], rc);
    }

    private void getAgentSessionLinks() throws IOException, ChatApiException{
        agentResources = new HashMap<String,String>();
        String req = rh.addVersion(agentSessionURI);
        byte[][] res = new byte[1][];
        int rc = rh.doGetRequest(req, res);
        if (rc == HttpStatus.SC_OK) {
            Node n = rh.getNode(res[0], "agentSessionId");
            if (n == null) {
                ChatApiException e = new ChatApiException(rc);
                e.message = "getAgentSessionLinks has no agentSessionId in the reply";
                throw e;
            }
            NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node item = nl.item(i);
                if ("link".equals(item.getNodeName())) {
                    String href = item.getAttributes().getNamedItem("href").getNodeValue();
                    String rel = item.getAttributes().getNamedItem("rel").getNodeValue();
                    agentResources.put(rel,rh.addVersion(href));
                }
            }
            return;
        }
        throw rh.buildErrorException(res[0], rc);
    }
}
