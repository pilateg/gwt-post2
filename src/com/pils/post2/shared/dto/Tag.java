package com.pils.post2.shared.dto;

import javax.persistence.NamedQuery;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "TAGS")
@NamedQuery(name = "getTags", query = "select t from Tag t where upper(t.title) like upper(:title+'%')")
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
