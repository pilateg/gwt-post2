package com.pils.post2.shared.dto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@javax.persistence.Entity
@Table(name = "ENTRIES")
public class Entry extends Entity {
	@NotNull
	@Column(length = 32)
	private String title;
	@NotNull
	@ManyToOne
	private Section section;
	@NotNull
	private String content;
	@ManyToMany//(fetch = FetchType.EAGER)
	private List<Tag> tags;
	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL)//, fetch = FetchType.EAGER)
	private List<Comment> comments;

	@Override
	public EntityType getType() {
		return EntityType.Entry;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return content.substring(0, 128);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
