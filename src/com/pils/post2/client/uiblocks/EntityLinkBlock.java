package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.pils.post2.client.conversation.dto.Entity;
import com.pils.post2.client.layout.Resources;

public class EntityLinkBlock extends EntityBlock {

	public EntityLinkBlock(Entity e) {
		super(e);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		mainPanel.addStyleName(EntityLinkResources.INSTANCE.css().entityLink());
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
