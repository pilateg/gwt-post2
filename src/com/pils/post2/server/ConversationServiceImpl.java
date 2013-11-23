package com.pils.post2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pils.post2.shared.conversation.ConversationService;
import com.pils.post2.shared.dto.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.security.SecureRandom;
import java.util.*;

public class ConversationServiceImpl extends RemoteServiceServlet implements ConversationService {

	private static SessionFactory ourSessionFactory;
	private static ServiceRegistry serviceRegistry;

	public static Session getSession() throws HibernateException {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();

			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
		return ourSessionFactory.openSession();
	}

	private void test() {
		final Session session = getSession();
		try {
			System.out.println("querying all the managed entities...");
			final Map metadataMap = session.getSessionFactory().getAllClassMetadata();
			for (Object key : metadataMap.keySet()) {
				final ClassMetadata classMetadata = (ClassMetadata) metadataMap.get(key);
				final String entityName = classMetadata.getEntityName();
				final Query query = session.createQuery("from " + entityName);
				System.out.println("executing: " + query.getQueryString());
				for (Object o : query.list()) {
					System.out.println("  " + o);
				}
			}
		} finally {
			session.close();
		}
	}

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

	@Override
	public boolean addUser(long sessionId, User user) {
		return true;
	}

	@Override
	public List<Section> fetchSections(long sessionId) {
		User user = sessions.get(sessionId);
		if (user != null) {
			List<Section> sections = new ArrayList<Section>(4);
			for (int i = 0; i < 4; i++) {
				Section section = new Section();
				section.setOwner(user);
				section.setTitle("section " + i);
				List<Entry> entries = new ArrayList<Entry>(20);
				for (int j = 0; j < 20; ++j) {
					Entry entry = new Entry();
					entry.setTitle("entry " + j);
					entry.setSection(section);
					entry.setContent(section.getTitle() + "<br>blabla" + j);
					entries.add(entry);
				}
				section.setEntries(entries);
				sections.add(section);
			}
			return sections;
		}
		return null;
	}

	@Override
	public List<User> fetchUsers(long sessionId, String query) {
		if (!sessions.containsKey(sessionId) || !"name".contains(query))
			return null;
		User user = new User();
		user.setName("name");
		List<User> users = new ArrayList<User>();
		users.add(user);
		return users;
	}

	@Override
	public List<Tag> fetchTags(long sessionId, String query) {
		if (!sessions.containsKey(sessionId) || !"tag".contains(query))
			return null;
		Tag tag = new Tag();
		tag.setTitle("tag");
		Tag tag2 = new Tag();
		tag2.setTitle("tag2");
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);
		tags.add(tag2);
		return tags;
	}

	@Override
	public List<? extends Entity> lightSearch(long sessionId, String query) {
		return fetchUsers(sessionId, query);
	}

	@Override
	public EntitiesList search(long sessionId, String query, long from, long number) {
		List<User> users = fetchUsers(sessionId, query);
		return new EntitiesList(users, users.size());
	}

	@Override
	public EntitiesList fetchEntities(long sessionId, Entity parent, int from, int number) {
		int itemsNumber = 20;
		List<Entity> entries = new ArrayList<Entity>(itemsNumber);
		int to = Math.min(from + number, itemsNumber);
		for (int i = from; i < to; ++i) {
			Entry entry = new Entry();
			entry.setTitle("entry" + i);
			entry.setContent("<b>" + i + "</b>");
			final Comment comment = new Comment();
			comment.setDate(new Date());
			comment.setContent("blabla" + i);
			List<Comment> comments = new ArrayList<Comment>();
			comments.add(comment);
			entry.setComments(comments);
			entries.add(entry);
		}
		return new EntitiesList(entries, itemsNumber);
	}

	@Override
	public Entry fetchEntry(long sessionId, long entryId) {
		return null;
	}
}