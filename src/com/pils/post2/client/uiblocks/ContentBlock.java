package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.layout.UiBlock;

import java.util.ArrayList;
import java.util.List;

public class ContentBlock extends UiBlock {
	private BreadcrumbBlock breadcrumb = (BreadcrumbBlock) BlockFactory.getInstance().getBreadcrumbBlock();
	private List<EntryBlock> entries = new ArrayList<EntryBlock>();
	private NavigationBlock navigation = (NavigationBlock) BlockFactory.getInstance().getNavigationBlock();

	public ContentBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(EntityLinkBlock.EntityLinkResources.INSTANCE.css().entityLink());
		mainPanel.add(breadcrumb);
		for (EntryBlock entry : entries)
			mainPanel.add(entry);
		mainPanel.add(navigation);
		initWidget(mainPanel);
	}
}
