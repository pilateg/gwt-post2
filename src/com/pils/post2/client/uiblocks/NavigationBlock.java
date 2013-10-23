package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class NavigationBlock extends UiBlock {
	public NavigationBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		initWidget(mainPanel);
	}
}
