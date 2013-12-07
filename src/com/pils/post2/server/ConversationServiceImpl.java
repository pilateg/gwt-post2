package com.pils.post2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pils.post2.shared.conversation.ConversationService;
import com.pils.post2.shared.dto.*;

import javax.ejb.EJB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationServiceImpl extends RemoteServiceServlet implements ConversationService {

	@EJB
	private DtoBean dtoBean;

	private Map<Long, User> sessions = new HashMap<Long, User>();

	public SessionUser login(String name, String password) {
		return dtoBean.login(name, password);
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
	public boolean addComment(long sessionId, Comment comment) { //todo checks
		if (sessions.containsKey(sessionId)) {
			dtoBean.addEntity(comment);
			return true;
		}
		return false;
	}

	@Override
	public boolean addEntry(long sessionId, Entry entry) { //todo checks
		if (sessions.containsKey(sessionId)) {
			dtoBean.addEntity(entry);
			return true;
		}
		return false;
	}

	@Override
	public boolean addSection(long sessionId, Section section) { //todo checks
		if (sessions.containsKey(sessionId)) {
			dtoBean.addEntity(section);
			return true;
		}
		return false;
	}

	@Override
	public boolean addUser(long sessionId, User user) { //todo checks
		dtoBean.addEntity(user);
		return true;
	}

	@Override
	public List<Section> fetchSections(long sessionId) {
		User user = sessions.get(sessionId);
		if (user != null)
			return dtoBean.fetchSections(user);
		return null;
	}

	@Override
	public List<User> fetchUsers(long sessionId, String query) {
		if (sessions.containsKey(sessionId))
			return dtoBean.fetchUsers(query);
		return null;
	}

	@Override
	public List<Tag> fetchTags(long sessionId, String query) {
		if (sessions.containsKey(sessionId))
			return dtoBean.fetchTags(query);
		return null;
	}

	@Override
	public List<? extends Entity> lightSearch(long sessionId, String query) {
		return dtoBean.lightSearch(query);
	}

	@Override
	public EntitiesList search(long sessionId, String query, int from, int number) { //todo checks
		return dtoBean.search(query, from, number);
	}

	@Override
	public EntitiesList fetchEntities(long sessionId, Entity parent, int from, int number) { //todo checks
		return dtoBean.fetchEntities(parent, from, number);
	}

	@Override
	public Entry fetchEntry(long sessionId, long entryId) { //todo checks
		if (sessions.containsKey(sessionId))
			return dtoBean.fetchEntry(entryId);
		return null;
	}
}