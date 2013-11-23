package com.pils.post2.shared.dto;

import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "USERS")
@NamedQuery(name = "getUser", query = "select u from User u where u.name=:name and u.password=:password")
public class User extends Entity {
	private String name;
	private String password;
	@OneToMany
	private List<Section> sections;
	@OneToMany
	private List<Comment> comments;

	@Override
	public EntityType getType() {
		return EntityType.User;
	}

	@Override
	public String getTitle() {
		return name;
	}

	@Override
	public String getDescription() {
		return "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
