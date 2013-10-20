package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.layout.UiBlockHandler;
import com.pils.post2.client.uiblocks.BlockFactory;
import com.pils.post2.client.uiblocks.CategoriesBlock;

public class Post2 implements EntryPoint {
	private UiBlockHandler blockHandler = GWT.create(UiBlockHandler.class);

	public void onModuleLoad() {
		RootLayoutPanel.get().add(blockHandler);

		DockLayoutPanel east = new DockLayoutPanel(Style.Unit.PX);
		east.addNorth(BlockFactory.getInstance().getLoginBlock(), 200);
		east.addNorth(BlockFactory.getInstance().getSearchBlock(), 200);
		((CategoriesBlock) BlockFactory.getInstance().getCategoriesBlock()).setCategories(null);
		east.addNorth(BlockFactory.getInstance().getCategoriesBlock(), 200);
		blockHandler.addEast(east, 200);
		DockLayoutPanel center = new DockLayoutPanel(Style.Unit.PX);
		center.addNorth(BlockFactory.getInstance().getBreadcrumbBlock(), 30);
		center.addSouth(BlockFactory.getInstance().getNavigationBlock(), 100);
		center.add(BlockFactory.getInstance().getContentBlock());
		blockHandler.add(center);

	}
}
