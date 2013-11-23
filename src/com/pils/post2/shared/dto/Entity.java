package com.pils.post2.shared.dto;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class Entity implements Serializable {
	@Id
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
