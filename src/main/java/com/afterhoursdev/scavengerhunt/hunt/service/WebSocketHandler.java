package com.afterhoursdev.scavengerhunt.hunt.service;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.afterhoursdev.scavengerhunt.hunt.websocket.messages.BaseHuntMessage;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;


@Component
//public class WebSocketHandler extends TextWebSocketHandler implements Runnable  {
//public class WebSocketHandler extends TextWebSocketHandler {
public class WebSocketHandler extends AbstractWebSocketHandler {
	private static final ConcurrentHashMap<String, Multimap<String, WebSocketSession>> managedHunts = new ConcurrentHashMap<>();
	private static final Gson gson = new Gson();
	//private volatile boolean eventBusThreadRunning = true;
		
	//@Autowired
	//EventBusService eventBusService;
	
	//@Autowired
	//Map<String, Consumer<String>> huntEvents;

	  
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    
    	//TODO: throw exception if path and query parameters are invalid
    	System.out.println("afterConnectionEstablished hit!");
    	URI uri = session.getUri();
    	System.out.println("uri: " + uri.toString());
    	
    	HashMap<String, String> queryParameters = parseQueryParameters(uri);  
    	
    	String huntId = queryParameters.get("huntId");
    	System.out.println("huntId: " + huntId);
    	
    	String teamId = queryParameters.get("teamId");
    	System.out.println("teamId: " + teamId);
    	
    	String playerName = queryParameters.get("playerName");
    	System.out.println("playerId: " + playerName);
    	
    	String huntAlone = queryParameters.get("huntAlone");
    	System.out.println("huntAlone: " + huntAlone);
    
       	           
        // Check if Hunt is already being managed  
     	Multimap<String, WebSocketSession> hunt = managedHunts.get(huntId);
   	    if (hunt == null) {
    	    Multimap<String, WebSocketSession> teams = Multimaps.synchronizedMultimap(HashMultimap.create());
    		teams.put(teamId, session);
    	    managedHunts.put(huntId, teams);
        } else {
    	    hunt.put(teamId, session);
     	}
    }
 
    //@Override
    //protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    //}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof PingMessage) {
            session.sendMessage(new PongMessage(((PingMessage) message).getPayload()));
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	//sessions.remove(session.getId());
    	session.close();  // is this necessary?
        System.out.println("Disconnected: " + session.getId());
    }
  
    public void sendWebSocketMessage(BaseHuntMessage huntMessage) {
    	
    	// Get the Hunt associated with the Hunt message
       	Multimap<String, WebSocketSession> hunt = managedHunts.get(huntMessage.getHuntId()); 
       	
       	
       	if (hunt != null) {
       		System.out.println("Sending web socket message.");
            // Get the web socket connections for all players associated with the team   
  	        Collection<WebSocketSession> webSocketSessions = hunt.get(huntMessage.getTeamId());    
  	      
            for (WebSocketSession webSocketSession : webSocketSessions) {
        	    try {
        		    if (webSocketSession.isOpen()) {
        	            webSocketSession.sendMessage(new TextMessage(gson.toJson(huntMessage)));
        		    }
			    } catch (IOException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   } 
            }
        } else
        	System.out.println("Did not find Hunt. Cannot send web socket message.");
    }
    
    private HashMap<String, String> parseQueryParameters(URI uri) {
        HashMap<String, String> queryParameters = new HashMap<>();
		 
        String query = uri.getQuery(); 
        System.out.println("Query: " + query);
        
        String[] queryParams = query.split("&");
        for (String param : queryParams) {
           String[] keyValue = param.split("=");
           String queryParamKey = keyValue[0];
           String queryParamValue = keyValue[1];
           
           queryParameters.put(queryParamKey, queryParamValue);
        }
        return queryParameters;
    }
}