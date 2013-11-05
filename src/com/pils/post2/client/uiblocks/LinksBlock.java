package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.pils.post2.client.conversation.dto.Entity;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

import java.util.ArrayList;
import java.util.List;

public class LinksBlock extends UiBlock {
	private FlowPanel mainPanel = GWT.create(FlowPanel.class);
	private Label title = GWT.create(Label.class);

	public LinksBlock(String header) {
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		title.setText(header);
		mainPanel.add(title);
		initWidget(mainPanel);
	}

	public void setCategories(List<Entity> entities) {
		mainPanel.clear();
		mainPanel.add(title);
		if (entities != null)
			for (Entity entity : entities)
				mainPanel.add(new EntityLinkBlock(entity));
	}
}
