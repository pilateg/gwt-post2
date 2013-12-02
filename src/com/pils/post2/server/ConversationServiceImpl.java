package com.pils.post2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pils.post2.shared.conversation.ConversationService;
import com.pils.post2.shared.dto.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.SecureRandom;
import java.util.*;

public class ConversationServiceImpl extends RemoteServiceServlet implements ConversationService {

	@PersistenceContext(name = "post2PU")
	private EntityManager entityManager;

	private Map<Long, User> sessions = new HashMap<Long, User>();

	public SessionUser login(String name, String password) {
		User user = entityManager.createNamedQuery("getUser", User.class)
				.setParameter("name", name)
				.setParameter("password", password).getSingleResult();
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
	public boolean addComment(long sessionId, Comment comment) { //todo checks
		if (sessions.containsKey(sessionId)) {
			entityManager.persist(comment);
			return true;
		}
		return false;
	}

	@Override
	public boolean addEntry(long sessionId, Entry entry) { //todo checks
		if (sessions.containsKey(sessionId)) {
			entityManager.persist(entry);
			return true;
		}
		return false;
	}

	@Override
	public boolean addSection(long sessionId, Section section) { //todo checks
		if (sessions.containsKey(sessionId)) {
			entityManager.persist(section);
			return true;
		}
		return false;
	}

	@Override
	public boolean addUser(long sessionId, User user) { //todo checks
		entityManager.persist(user);
		return true;
	}

	@Override
	public List<Section> fetchSections(long sessionId) {
		User user = sessions.get(sessionId);
		if (user != null)
			return entityManager.createNamedQuery("getSections", Section.class)
					.setParameter("id", user.getId()).getResultList();
		return null;
	}

	@Override
	public List<User> fetchUsers(long sessionId, String query) {
		if (sessions.containsKey(sessionId))
			return entityManager.createNamedQuery("getUsers", User.class)
					.setParameter("query", query).getResultList();
		return null;
	}

	@Override
	public List<Tag> fetchTags(long sessionId, String query) {
		if (sessions.containsKey(sessionId))
			return entityManager.createNamedQuery("getTags", Tag.class)
					.setParameter("title", query).getResultList();
		return null;
	}

	@Override
	public List<? extends Entity> lightSearch(long sessionId, String query) {
		List<Entity> entities = new ArrayList<Entity>(4);
		entities.add(entityManager.createQuery("select e from Tag e where upper(e.title) like upper(:query+'%')", Tag.class).
				setParameter("query", query).getSingleResult());
		entities.add(entityManager.createQuery("select e from Section e where upper(e.title) like upper(:query+'%')", Section.class).
				setParameter("query", query).getSingleResult());
		entities.add(entityManager.createQuery("select e from Entry e where upper(e.title) like upper(:query+'%')", Entry.class).
				setParameter("query", query).getSingleResult());
		entities.add(entityManager.createQuery("select e from User e where upper(e.name) like upper(:query+'%')", User.class).
				setParameter("query", query).getSingleResult());
		return entities;
	}

	@Override
	public EntitiesList search(long sessionId, String query, int from, int number) { //todo checks, enable hibernate caching
		List<Entity> entities = new ArrayList<Entity>();
		entities.addAll(entityManager.createQuery("select e from Tag e where upper(e.title) like upper(:query+'%')", Tag.class).
				setParameter("query", query).getResultList());
		entities.addAll(entityManager.createQuery("select e from Section e where upper(e.title) like upper(:query+'%')", Section.class).
				setParameter("query", query).getResultList());
		entities.addAll(entityManager.createQuery("select e from Entry e where upper(e.title) like upper(:query+'%')", Entry.class).
				setParameter("query", query).getResultList());
		entities.addAll(entityManager.createQuery("select e from User e where upper(e.name) like upper(:query+'%')", User.class).
				setParameter("query", query).getResultList());
		return new EntitiesList(entities.subList(from, from + number), entities.size());
	}

	@Override
	public EntitiesList fetchEntities(long sessionId, Entity parent, int from, int number) { //todo checks
		List<Entity> entities = new ArrayList<Entity>();
		switch (parent.getType()) {
			case Entry:
				entities.addAll(entityManager.createQuery("select c from Comment c where c.entry=:parent", Comment.class).
						setParameter("parent", parent).getResultList());
				break;
			case Tag:
				entities.addAll(entityManager.createQuery("select e from Entry e where :parent member of e.tags", Entry.class).
						setParameter("parent", parent).getResultList());
				break;
			case Section:
				entities.addAll(entityManager.createQuery("select e from Entry e where e.section=:parent", Entry.class).
						setParameter("parent", parent).getResultList());
				break;
		}
		return new EntitiesList(entities.subList(from, from + number), entities.size());
	}

	@Override
	public Entry fetchEntry(long sessionId, long entryId) { //todo checks
		if (sessions.containsKey(sessionId))
			return entityManager.find(Entry.class, entryId);
		return null;
	}
}