package com.pils.post2.shared.dto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@javax.persistence.Entity
@Table(name = "USERS")
@NamedQueries({
		@NamedQuery(name = "getUser", query = "select u from User u where u.name=:name and u.password=:password"),
		@NamedQuery(name = "getUsers", query = "select u from User u where upper(u.name) like upper(:query+'%')")})
public class User extends Entity {
	@NotNull
	@Column(length = 32, unique = true)
	private String name;
	@NotNull
	private String password;
	@OneToMany(mappedBy = "owner")
	private List<Section> sections;
	@OneToMany(mappedBy = "author")
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
