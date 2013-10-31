package com.pils.post2.client.conversation.dto;

public class Entry extends Entity {
	private String name;
	private String content;

	@Override
	public EntityType getType() {
		return EntityType.Entry;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getDescription() {
		return content.substring(0, 128);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
