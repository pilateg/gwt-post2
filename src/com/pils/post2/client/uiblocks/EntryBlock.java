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

public class EntryBlock extends UiBlock {
	private Entity entity;
	private Anchor link = GWT.create(Anchor.class);

	public EntryBlock(Entity e) {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		final Button options = new Button();
		options.setText("o");
		final PopupPanel popup = new PopupPanel();
		popup.setAutoHideEnabled(true);
		options.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				switch (entity.getType()) {
					case Category:
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
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("");
			}
		});
		entity = e;
		link.setHTML("<h2>" + entity.toString() + "</h2>");
		HTML description = new HTML(entity.getDescription());
		description.getElement().getStyle().setDisplay(Style.Display.BLOCK);
		mainPanel.add(description);
	}

	public void setTitleClickHandler(ClickHandler handler) {
		link.addClickHandler(handler);
	}
}
