package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.client.layout.widgets.Button;

public abstract class EntityBlock extends Composite {

	protected Entity entity;
	protected Anchor link = GWT.create(Anchor.class);
	protected FlowPanel mainPanel;
	protected HTML description;

	protected EntityBlock(final Entity e) {
		mainPanel = new FlowPanel();
		final Button options = new Button();
		options.setText("o");
		final PopupPanel popup = new PopupPanel();
		popup.addStyleName(Resources.INSTANCE.css().popup());
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
				WorkspaceBlock.INSTANCE.selectEntity(e);
			}
		});
		link.setText(entity.getTitle());
		description = new HTML();
		description.getElement().getStyle().setDisplay(Style.Display.BLOCK);
		mainPanel.add(description);
	}
}
