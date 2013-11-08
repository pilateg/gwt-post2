package com.pils.post2.shared.dto;

public class Tag extends Entity {
	private String title;

	@Override
	public EntityType getType() {
		return EntityType.Tag;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return "";
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
