package com.pils.post2.client.conversation.dto;

public class Category extends Entity {
	private String name;
	private String description;

	@Override
	public EntityType getType() {
		return EntityType.Category;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
