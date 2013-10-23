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

public class EntityLinkBlock extends UiBlock {
	private Entity entity;

	private Hyperlink link;

	public EntityLinkBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		mainPanel.addStyleName(EntityLinkResources.INSTANCE.css().entityLink());
		final Button options = GWT.create(Button.class);
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
		link = GWT.create(Hyperlink.class);
		mainPanel.add(link);
		initWidget(mainPanel);
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
		link.setText(entity.toString());
	}

	public interface EntityLinkResources extends ClientBundle {
		EntityLinkResources INSTANCE = GWT.create(EntityLinkResources.class);

		@Source("../layout/styles/entityLink.css")
		Css css();

		public interface Css extends CssResource {
			String entityLink();
		}
	}
}
