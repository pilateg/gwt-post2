package com.pils.post2.shared.dto;

import java.io.Serializable;

public abstract class Entity implements Serializable {
	protected long id;

	public abstract EntityType getType();

	public abstract String getTitle();

	public abstract String getDescription();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public enum EntityType {
		Section, Tag, Entry, Comment, User
	}
}
