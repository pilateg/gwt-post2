package com.pils.post2.shared.dto;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Entity implements Serializable {
	@Id
	@SequenceGenerator(name = "seq_gen", initialValue = 0, allocationSize = 1)
	@GeneratedValue(generator = "seq_gen")
	protected long id;
	@Version
	protected long version;

	public abstract EntityType getType();

	public abstract String getTitle();

	public abstract String getDescription();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public enum EntityType {
		Section, Tag, Entry, Comment, User
	}
}
