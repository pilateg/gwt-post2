package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.conversation.dto.Entity;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;
import com.pils.post2.client.layout.widgets.Button;

public class EntityLinkBlock extends UiBlock {
	private Entity entity;
	private Anchor link = GWT.create(Anchor.class);

	public EntityLinkBlock(Entity e) {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		mainPanel.addStyleName(EntityLinkResources.INSTANCE.css().entityLink());
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
		link.setText(entity.getTitle());
	}

	public void setClickHandler(ClickHandler handler) {
		link.addClickHandler(handler);
	}

	public interface EntityLinkResources extends ClientBundle {
		EntityLinkResources INSTANCE = GWT.create(EntityLinkResources.class);

		@Source("../layout/styles/entityLink.css")
		Css css();

		interface Css extends CssResource {
			String entityLink();
		}
	}
}
