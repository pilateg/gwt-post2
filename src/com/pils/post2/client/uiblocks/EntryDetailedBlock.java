package com.pils.post2.client.uiblocks;

import com.google.gwt.dom.client.Style;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.User;

public class EntryDetailedBlock extends EntityBlock {

	public EntryDetailedBlock(Entity e) {
		super(e);
		mainPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		switch (e.getType()) {
			case Entry:
				Entry entry = (Entry) e;
				description.setHTML(entry.getContent());
				DiscussionBlock discussion = new DiscussionBlock(entry);
				discussion.setComments(entry.getComments());
				mainPanel.add(discussion);
				break;
			case User:
				User user = (User) e;
				description.setHTML("<h1>" + user.getName() + "</h1>");
				break;
		}
	}
}
