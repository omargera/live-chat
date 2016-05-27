package com.livechat.adapter.liveperson;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.livechat.adapter.ChatApiException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class RequestHelper {
    private Map<String, String> headersForGet = new HashMap<String, String>(2);
    private Map<String, String> headersForPost = new HashMap<String, String>(3);
    private Map<String, String> headersForPut = new HashMap<String, String>(4);
    protected static HttpConnectionManager defaultConnectionManager = new MultiThreadedHttpConnectionManager();
    final static String version = "1";

    HttpClient httpclient;

    public RequestHelper(String appKey) {
        setHeadersMap(appKey);
        httpclient = new HttpClient(defaultConnectionManager);
    }

    public String addVersion(String url){
        if (url.indexOf("?") == -1){
            return url+"?v="+version;
        }
        else{
            return url+"&v="+version;
        }
    }
    private void setHeadersMap(String appKey) {
        String appKeyHeader = "LivePerson appKey=" + appKey;

        headersForGet.put("Accept", "application/xml");
        headersForGet.put("Authorization", appKeyHeader);

        headersForPost.put("Accept", "application/xml");
        headersForPost.put("Content-type", "application/xml; charset=UTF-8");
        headersForPost.put("Authorization", appKeyHeader);

        headersForPut.put("X-HTTP-Method-Override", "PUT");
        headersForPut.put("Accept", "application/xml");
        headersForPut.put("Content-type", "application/xml; charset=UTF-8");
        headersForPut.put("Authorization", appKeyHeader);
    }

    protected int doGetRequest(String req, byte[][] res) throws IOException {
        GetMethod method = new GetMethod(req);
        try {
            addHeaders(method, headersForGet);
            setFollowRedirect(method);
            int rc = httpclient.executeMethod(method);
            if (rc == 302){
                String newReq = method.getResponseHeader("location").getValue();
                return doGetRequest(newReq, res);
            }
            res[0] = method.getResponseBody();
            return rc;
        }
        finally {
            method.releaseConnection();
        }
    }

    protected int doPostRequest(String req, String body, byte[][] res) throws IOException {
        return doPostRequest(req, body, res, null, null);
    }

    protected int doPostRequest(String req, String body, byte[][] res, String header, StringBuffer headerVal) throws IOException {
        PostMethod method = new PostMethod(req);
        try {
            addHeaders(method, headersForPost);
            method.setRequestEntity(new StringRequestEntity(body, null, "utf-8"));
             System.out.println("Send Post:"+req+"\nData:"+body);
            int rc = httpclient.executeMethod(method);
            res[0] = method.getResponseBody();
            if (header != null) {
                Header h = method.getResponseHeader(header);
                if (h != null) {
                    headerVal.append(h.getValue());
                }
            }
            return rc;
        }
        finally {
            method.releaseConnection();
        }
    }

    protected int doPutRequest(String req, String body, byte[][] res) throws IOException {
        PostMethod method = new PostMethod(req);
        try {
            addHeaders(method, headersForPut);
            method.setRequestEntity(new StringRequestEntity(body, null, "utf-8"));
            int rc = httpclient.executeMethod(method);
            res[0] = method.getResponseBody();
            return rc;
        }
        finally {
            method.releaseConnection();
        }
    }

    public ChatApiException buildErrorException(byte[] body, int httpError) {
        Node n;
        try {
            n = getNode(body, "error");
        }
        catch (ChatApiException e) {
            e.httpStatusCode = httpError;
            return e;
        }
        ChatApiException exp = new ChatApiException(httpError);
        if (n == null) {
            return exp;
        }

        NodeList nl = n.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if ("internalCode".equals(item.getNodeName())) {
                exp.rc = Integer.parseInt(item.getTextContent());
            } else if ("message".equals(item.getNodeName())) {
                exp.message = item.getTextContent();
            } else if ("reason".equals(item.getNodeName())) {
                exp.reason = item.getTextContent();
            } else if ("time".equals(item.getNodeName())) {
                exp.time = item.getTextContent();
            }
        }
        return exp;
    }

    public Document getDocument(byte[] body) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder b;
        b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return b.parse(new ByteArrayInputStream(body));
    }

    public Node getNode(byte[] body, String tag) throws ChatApiException {
        try {
            Document d = getDocument(body);
            if (d == null) {
                return null;
            }

            NodeList nl = d.getElementsByTagName(tag);
            if (nl.getLength() != 1) {
                return null;
            }
            return nl.item(0);
        }
        catch (Exception e) {
            ChatApiException lpe = new ChatApiException();
            lpe.initCause(e);
            lpe.message = "Unable to parse the XML result";
            lpe.rc = -1;
            lpe.time = "NA";
            throw lpe;
        }
    }

    public List<Node> getChildNode(Node n, String tag) {
        NodeList nl = n.getChildNodes();
        List<Node> ret = new ArrayList<Node>(3);

        if (nl == null) {
            return ret;
        }

        for (int i = 0; i < nl.getLength(); i++) {
            Node r = nl.item(i);
            if (r.getNodeName().equals(tag)) {
                ret.add(r);
            }
        }
        return ret;
    }

    protected void addHeaders(HttpMethod method, Map<String, String> headers) {
        for (Map.Entry<String, String> e : headers.entrySet()) {
            method.addRequestHeader(e.getKey(), e.getValue());
        }
    }
    protected void setFollowRedirect(HttpMethod method){

    }
}
