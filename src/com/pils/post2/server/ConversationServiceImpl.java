package com.pils.post2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pils.post2.client.ConversationService;

public class ConversationServiceImpl extends RemoteServiceServlet implements ConversationService {
	public String getMessage(String msg) {
		return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
	}
}