package com.pils.post2.shared.dto;

import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SECTIONS")
@NamedQuery(name = "getSection", query = "select s from Section s where s.title=:title")
public class Section extends Entity {
	private String title;
	@ManyToOne
	private User owner;
	private boolean openForAll;
	@OneToMany
	private List<User> usersWithAccess;
	@OneToMany
	private List<Entry> entries;

	@Override
	public EntityType getType() {
		return EntityType.Section;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return null;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isOpenForAll() {
		return openForAll;
	}

	public void setOpenForAll(boolean openForAll) {
		this.openForAll = openForAll;
	}

	public List<User> getUsersWithAccess() {
		return usersWithAccess;
	}

	public void setUsersWithAccess(List<User> usersWithAccess) {
		this.usersWithAccess = usersWithAccess;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}
