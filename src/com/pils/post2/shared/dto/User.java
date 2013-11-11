package com.pils.post2.shared.dto;

import java.util.List;

public class User extends Entity {
	private String name;
	private String password;
	private List<Section> sections;
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
