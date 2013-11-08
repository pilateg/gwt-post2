package com.pils.post2.client.uiblocks;

import com.google.gwt.dom.client.Style;
import com.pils.post2.shared.dto.Entity;

public class EntryBlock extends EntityBlock {

	public EntryBlock(Entity e) {
		super(e);
		mainPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		description.setHTML(e.getDescription());
	}
}
