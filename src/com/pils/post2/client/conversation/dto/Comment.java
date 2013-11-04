package com.pils.post2.client.conversation.dto;

import java.util.Date;

public class Comment extends Entity {
	private User author;
	private Date date;
	private String content;
	private Entry entry;

	@Override
	public EntityType getType() {
		return EntityType.Comment;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
}