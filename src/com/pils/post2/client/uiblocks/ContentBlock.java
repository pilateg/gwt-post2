package com.pils.post2.client.uiblocks;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.Section;

import java.util.List;

public class ContentBlock extends Composite {

	private FlowPanel mainPanel;
	private Button addEntry;
	private Section currentSection;

	public ContentBlock() {
		mainPanel = new FlowPanel();
		ScrollPanel scrollPanel = new ScrollPanel(mainPanel);
		scrollPanel.addStyleName(Resources.INSTANCE.css().block());
		addEntry = new Button("add entry");
		mainPanel.add(addEntry);
		final PopupBlock popup = new PopupBlock();
		addEntry.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ConversationManager.getCurrentUser() != null)
					popup.center();
			}
		});
		final TextBox title = new TextBox();
		popup.addWidget(title);
		final TextArea content = new TextArea();
		popup.addWidget(content);
		popup.setButtons("create entry", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (content.getText() == null || content.getText().isEmpty())
							return;
						final Entry entry = new Entry();
						entry.setTitle(title.getText());
						entry.setContent(content.getText());
						entry.setSection(currentSection);
						ConversationManager.addEntry(entry, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									popup.hide();
									title.setText("");
									content.setText("");
									//add entry to db and update
									mainPanel.add(new EntryBlock(entry));
								}
							}
						});
					}
				},
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent cancelEvent) {
						popup.hide();
						title.setText("");
						content.setText("");
					}
				}
		);
		initWidget(scrollPanel);
	}

	public void setEntries(List<Entity> entities) {
		resetPanel(entities == null || entities.isEmpty() ? null : entities.get(0));
		if (entities != null)
			for (Entity entity : entities)
				mainPanel.add(new EntryBlock(entity));
	}

	public void setEntry(Entity entity) {
		resetPanel(entity);
		if (entity != null)
			mainPanel.add(new EntryDetailedBlock(entity));
	}

	private void resetPanel(Entity entity) {
		mainPanel.clear();
		mainPanel.add(addEntry);
		switch (entity.getType()) {
			case Entry:
				currentSection = ((Entry) entity).getSection();
				break;
			case Section:
				currentSection = (Section) entity;
				break;
			default:
				currentSection = null;
		}
	}
}
