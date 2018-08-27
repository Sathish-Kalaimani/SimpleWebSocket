package webSocketsPractice;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/serverendpoint")
public class ServerEndPoint {
	
	static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
	public void handleOpen(Session userSession) {
		System.out.println("Client connected to the Server..!!");
		users.add(userSession);
	}
	
	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		/*System.out.println("Message from client "+message);
		String reply = "echo "+message;
		System.out.println("Reply from Server "+reply);*/
		String userName = (String)userSession.getUserProperties().get("username");
		if(userName == null) {
			userSession.getUserProperties().put("username", message);
			userSession.getBasicRemote().sendText(buildJsonData("System","You are now connected as "+message));
		}else {
			/*for(Session user: users) {
				user.getBasicRemote().sendText(buildJsonData(userName,message));
			}*/
			Iterator<Session> iterator = users.iterator();
			while(iterator.hasNext())
				iterator.next().getBasicRemote().sendText(buildJsonData(userName, message));
		}
	}
	
	private String buildJsonData(String userName, String message) {
		JsonObject jsonObject = Json.createObjectBuilder().add("message", userName+": "+message).build();
		StringWriter stringWriter = new StringWriter();
		try 
			(JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
			jsonWriter.write(jsonObject);
		}
		return stringWriter.toString();
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("Client disconnected..!!");
		users.remove(userSession);
	}

	@OnError
	public void handleError(Throwable e) {
		e.printStackTrace();
	}
}
