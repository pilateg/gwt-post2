package com.pils.post2.shared.dto;

import java.io.Serializable;
import java.util.List;

public class EntitiesList implements Serializable {
	public int number;
	public List<? extends Entity> entities;

	public EntitiesList() {
	}

	public EntitiesList(List<? extends Entity> entities, int number) {
		this.entities = entities;
		this.number = number;
	}
}
