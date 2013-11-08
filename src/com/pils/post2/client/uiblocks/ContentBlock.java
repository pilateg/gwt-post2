package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.Section;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;
import com.pils.post2.client.layout.widgets.Button;

import java.util.List;

public class ContentBlock extends UiBlock {

	private FlowPanel mainPanel;
	private Button addEntry;
	private PopupPanel popupPanel;
	private Section currentSection;

	public ContentBlock() {
		ContentResources.INSTANCE.css().ensureInjected();
		mainPanel = new FlowPanel();
		ScrollPanel scrollPanel = new ScrollPanel(mainPanel);
		scrollPanel.addStyleName(Resources.INSTANCE.css().block());
		addEntry = new Button("add entry");
		mainPanel.add(addEntry);
		addEntry.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ConversationManager.getCurrentUser() != null)
					popupPanel.center();
			}
		});
		popupPanel = new PopupPanel(false, true);
		popupPanel.addStyleName(Resources.INSTANCE.css().block());
		popupPanel.addStyleName(ContentResources.INSTANCE.css().popup());
		FlowPanel panel = new FlowPanel();
		popupPanel.setWidget(panel);
		final TextBox title = new TextBox();
		panel.add(title);
		final TextArea content = new TextArea();
		panel.add(content);
		Button createButton = new Button("create entry");
		createButton.addClickHandler(new ClickHandler() {
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
							popupPanel.hide();
							title.setText("");
							content.setText("");
							mainPanel.add(new EntryBlock(entry));
						}
					}
				});
			}
		});
		panel.add(createButton);
		Button cancelButton = new Button("cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent cancelEvent) {
				popupPanel.hide();
				title.setText("");
				content.setText("");
			}
		});
		panel.add(cancelButton);
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

	public interface ContentResources extends ClientBundle {
		ContentResources INSTANCE = GWT.create(ContentResources.class);

		@Source("../layout/styles/content.css")
		Css css();

		interface Css extends CssResource {
			String popup();
		}
	}
}
