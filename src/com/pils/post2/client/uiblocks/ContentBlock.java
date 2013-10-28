package com.pils.post2.client.uiblocks;

import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.layout.UiBlock;

import java.util.ArrayList;
import java.util.List;

public class ContentBlock extends UiBlock {
	private List<EntryBlock> entries = new ArrayList<EntryBlock>();

	public ContentBlock() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName(EntityLinkBlock.EntityLinkResources.INSTANCE.css().entityLink());
		for (EntryBlock entry : entries)
			mainPanel.add(entry);
		initWidget(mainPanel);
	}
}
