package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.conversation.dto.Entity;
import com.pils.post2.client.layout.UiBlock;
import com.pils.post2.client.layout.widgets.Button;

public class EntityBlock extends UiBlock {

	protected Entity entity;
	protected Anchor link = GWT.create(Anchor.class);
	protected FlowPanel mainPanel;
	protected HTML description;

	public EntityBlock(Entity e) {
		mainPanel = new FlowPanel();
		final Button options = new Button();
		options.setText("o");
		final PopupPanel popup = new PopupPanel();
		popup.setAutoHideEnabled(true);
		options.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				switch (entity.getType()) {
					case Section:
						popup.setWidget(new Label("category options"));
						break;
					case Entry:
						popup.setWidget(new Label("entry options"));
						break;
					case Tag:
						popup.setWidget(new Label("tag options"));
						break;
				}
				popup.showRelativeTo(options);
			}
		});
		mainPanel.add(options);
		mainPanel.add(link);
		initWidget(mainPanel);
		entity = e;
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("");
			}
		});
		link.setText(entity.getTitle());
		description = new HTML();
		description.getElement().getStyle().setDisplay(Style.Display.BLOCK);
		mainPanel.add(description);
	}

	public void setClickHandler(ClickHandler handler) {
		link.addClickHandler(handler);
	}
}
