package com.pils.post2.client.conversation.dto;

import java.util.List;

public class Section extends Entity {
	private String title;
	private User owner;
	private boolean openForAll;
	transient
	private List<User> usersWithAccess;
	transient
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
