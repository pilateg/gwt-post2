package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

import java.util.ArrayList;
import java.util.List;

public class CategoriesBlock extends UiBlock {
	private List<EntityLinkBlock> categories = new ArrayList<EntityLinkBlock>();
	private FlowPanel mainPanel;

	public CategoriesBlock() {
		mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		initWidget(mainPanel);
	}

	public void setCategories(List<EntityLinkBlock> categories) {
		this.categories = categories;
		if (categories != null)
			for (EntityLinkBlock entity : categories)
				mainPanel.add(entity);
	}
}
