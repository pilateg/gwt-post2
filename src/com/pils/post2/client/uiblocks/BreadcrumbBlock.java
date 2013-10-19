package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class BreadcrumbBlock extends UiBlock {
	public BreadcrumbBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().entityLink());
		initWidget(mainPanel);
	}
}
