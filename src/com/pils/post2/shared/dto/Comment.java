package com.pils.post2.shared.dto;

import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "COMMENTS")
public class Comment extends Entity {
	@NotNull
	@ManyToOne
	private User author;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	private String content;
	@NotNull
	@ManyToOne
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