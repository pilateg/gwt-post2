package com.pils.post2.server;

import com.pils.post2.shared.dto.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "SessionBeanEJB")
@Local(DtoBean.class)
public class DtoBeanImpl implements DtoBean {

	@PersistenceContext(name = "post2PU")
	private EntityManager entityManager;

	public DtoBeanImpl() {
	}

	@Override
	public SessionUser login(String name, String password) {
		User user;
		if ("name".equals(name) && "pass".equals(password)) {
			user = new User();
			user.setName(name);
			entityManager.persist(user);
			entityManager.flush();
		}

		user = entityManager.createNamedQuery("getUser", User.class)
				.setParameter("name", name)
				.setParameter("password", password).getSingleResult();
		if (user == null)
			return null;
		long sessionId = new SecureRandom().nextLong();
		return new SessionUser(sessionId, user);
	}

	@Override
	public void addEntity(Entity entity) {
		entityManager.persist(entity);
	}

	@Override
	public List<Section> fetchSections(User user) {
		if (user != null)
			return entityManager.createNamedQuery("getSections", Section.class)
					.setParameter("id", user.getId()).getResultList();
		return null;
	}

	@Override
	public List<User> fetchUsers(String query) {
		return entityManager.createNamedQuery("getUsers", User.class)
				.setParameter("query", query).getResultList();
	}

	@Override
	public List<Tag> fetchTags(String query) {
		return entityManager.createNamedQuery("getTags", Tag.class)
				.setParameter("title", query).getResultList();
	}

	@Override
	public List<? extends Entity> lightSearch(String query) {
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
	public EntitiesList search(String query, int from, int number) { //todo enable hibernate caching
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
	public EntitiesList fetchEntities(Entity parent, int from, int number) {
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
	public Entry fetchEntry(long entryId) {
		return entityManager.find(Entry.class, entryId);
	}
}
