package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.client.layout.Resources;

import java.util.List;

public class LinksBlock extends Composite {
	private FlowPanel mainPanel = GWT.create(FlowPanel.class);
	private Label title = GWT.create(Label.class);

	public LinksBlock() {
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		mainPanel.add(title);
		initWidget(mainPanel);
	}

	public void setTitle(String header) {
		title.setText(header);
	}

	public void setCategories(List<Entity> entities) {
		mainPanel.clear();
		mainPanel.add(title);
		if (entities != null)
			for (Entity entity : entities)
				mainPanel.add(new EntityLinkBlock(entity));
	}
}
