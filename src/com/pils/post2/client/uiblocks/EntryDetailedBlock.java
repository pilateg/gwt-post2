package com.pils.post2.client.uiblocks;

import com.google.gwt.dom.client.Style;
import com.pils.post2.client.conversation.dto.Entity;
import com.pils.post2.client.conversation.dto.Entry;

public class EntryDetailedBlock extends EntityBlock {

	public EntryDetailedBlock(Entity e) {
		super(e);
		mainPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		switch (e.getType()) {
			case Entry:
				description.setHTML(((Entry) e).getContent());
				break;
		}
	}
}
