package com.pils.post2.client.conversation.dto;

public class Category extends Entity {
	private String name;

	@Override
	public EntityType getType() {
		return EntityType.Category;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
