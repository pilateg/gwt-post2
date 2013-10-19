package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class EntityLinkBlock extends UiBlock {
	public EntityLinkBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().entityLink());
		Label text = GWT.create(Label.class);
		mainPanel.add(text);
		initWidget(mainPanel);
	}
}
