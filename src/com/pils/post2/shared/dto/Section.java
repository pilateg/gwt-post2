package com.pils.post2.shared.dto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SECTIONS")
@NamedQuery(name = "getSections", query = "select s from Section s, User u where s.openForAll=true or s.owner.id=:id and u.id=:id and u member of s.usersWithAccess")
public class Section extends Entity {
	@NotNull
	@Column(length = 32)
	private String title;
	@NotNull
	@ManyToOne
	private User owner;
	private boolean openForAll;
	@OneToMany
	private List<User> usersWithAccess;
	@OneToMany(mappedBy = "section")
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
