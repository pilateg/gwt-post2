package com.pils.post2.client.conversation.dto;

public abstract class Entity {
	private long id;

	public abstract EntityType getType();

	@Override
	public String toString() {
		return getType().toString() + "#" + id;
	}

	public abstract String getDescription();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public enum EntityType {
		Category, Tag, Entry
	}
}
