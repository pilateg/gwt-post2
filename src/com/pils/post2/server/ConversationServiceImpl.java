package com.pils.post2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pils.post2.shared.dto.*;
import com.pils.post2.shared.conversation.ConversationService;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class ConversationServiceImpl extends RemoteServiceServlet implements ConversationService {

	private Map<Long, User> sessions = new HashMap<Long, User>();

	public SessionUser login(String name, String password) {
		User user = null;

		//get user from db
		if ("name".equals(name) && "pass".equals(password)) {
			user = new User();
			user.setName(name);
		}

		if (user == null)
			return null;
		long sessionId = new SecureRandom().nextLong();
		sessions.put(sessionId, user);
		return new SessionUser(sessionId, user);
	}

	@Override
	public User getUser(long sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public boolean logout(long sessionId) {
		User user = sessions.get(sessionId);
		if (user != null) {
			sessions.remove(sessionId);
			return true;
		}
		return false;
	}

	@Override
	public boolean addComment(long sessionId, Comment comment) {
		return sessions.containsKey(sessionId);
	}

	@Override
	public boolean addEntry(long sessionId, Entry entry) {
		return true;
	}

	@Override
	public boolean addSection(long sessionId, Section section) {
		return true;
	}
}