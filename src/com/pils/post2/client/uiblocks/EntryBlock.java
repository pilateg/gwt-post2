package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class EntryBlock extends UiBlock {
	public EntryBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		Label title = GWT.create(Label.class);
		mainPanel.add(title);
		initWidget(mainPanel);
	}
}
