package com.pils.post2.client.uiblocks;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class EntryBlock extends UiBlock {
	public EntryBlock() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		Label title = new Label();
		mainPanel.add(title);
		initWidget(mainPanel);
	}
}
