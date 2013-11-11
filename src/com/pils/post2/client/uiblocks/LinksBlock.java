package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.shared.dto.Section;
import com.pils.post2.shared.dto.User;

import java.util.List;

public class LinksBlock extends Composite {
	private FlowPanel mainPanel = GWT.create(FlowPanel.class);
	private Label title = GWT.create(Label.class);
	private Button addSection;

	public LinksBlock() {
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		addSection = new Button("add section");
		mainPanel.add(addSection);
		addSection.getElement().getStyle().setFloat(Style.Float.RIGHT);
		final PopupBlock popup = new PopupBlock();
		addSection.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ConversationManager.getCurrentUser() != null)
					popup.center();
			}
		});
		final TextBox sectionTitle = new TextBox();
		popup.addWidget(sectionTitle);
		popup.setButtons("create section", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (sectionTitle.getText() == null || sectionTitle.getText().isEmpty())
							return;
						final Section section = new Section();
						section.setTitle(sectionTitle.getText());
						ConversationManager.addSection(section, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									popup.hide();
									sectionTitle.setText("");
									//add section to db and update
									addCategory(section);
								}
							}
						});
					}
				},
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent cancelEvent) {
						popup.hide();
						sectionTitle.setText("");
					}
				}
		);
		mainPanel.add(title);
		initWidget(mainPanel);
	}

	public void setUser(User user) {
		if (user == null) {
			title.setText("links");
			addSection.setVisible(false);
		} else {
			title.setText("my sections");
			addSection.setVisible(true);
		}
		ConversationManager.fetchSections(new ConversationCallback<List<Section>>() {
			@Override
			public void onSuccess(List<Section> sections) {
				setCategories(sections);
			}
		});
	}

	public void setCategories(List<? extends Entity> entities) {
		mainPanel.clear();
		mainPanel.add(addSection);
		mainPanel.add(title);
		if (entities != null)
			for (Entity entity : entities)
				mainPanel.add(new EntityLinkBlock(entity));
	}

	public void addCategory(Entity entity) {
		if (entity != null)
			mainPanel.add(new EntityLinkBlock(entity));
	}
}
