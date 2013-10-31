package com.pils.post2.client.conversation.dto;

public class Tag extends Entity {
	private String name;

	@Override
	public EntityType getType() {
		return EntityType.Tag;
	}

	@Override
	public String toString() {
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
}
