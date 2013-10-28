package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.conversation.dto.Tag;
import com.pils.post2.client.uiblocks.*;

public class Post2 implements EntryPoint {
	private DockLayoutPanel blockHandler = GWT.create(DockLayoutPanel.class);

	public void onModuleLoad() {
		RootLayoutPanel.get().add(blockHandler);

		DockLayoutPanel east = new DockLayoutPanel(Style.Unit.PX);
		east.addNorth(new LoginBlock(), 200);
		east.addNorth(new SearchBlock(), 200);
    CategoriesBlock categoriesBlock = new CategoriesBlock();
		categoriesBlock.setCategories(null);
		east.addNorth(categoriesBlock, 200);
		blockHandler.addEast(east, 200);
		DockLayoutPanel center = new DockLayoutPanel(Style.Unit.PX);
		EntityLinkBlock entityLink = new EntityLinkBlock();
		Tag tag = new Tag();
		tag.setName("tag_name");
		entityLink.setEntity(tag);
		EntityLinkBlock entityLink2 = new EntityLinkBlock();
		tag.setName("tag_name2");
		entityLink2.setEntity(tag);
		BreadcrumbBlock breadcrumb = new BreadcrumbBlock();
		breadcrumb.addBreadcrumb(entityLink);
		breadcrumb.addBreadcrumb(entityLink2);
		center.addNorth(breadcrumb, 40);
		NavigationBlock navigation = new NavigationBlock();
		navigation.setUp(1000, 2);
		navigation.setPageSelectionHandler(new NavigationBlock.PageSelectionHandler() {
			@Override
			public void onPageSelected(int pageNumber, int itemsOnPage) {
				//int offset = pageNumber*itemsOnPage;
				//((ContentBlock) BlockFactory.getInstance().getContentBlock()).update(pageNumber*itemsOnPage, itemsOnPage);
			}
		});
		navigation.setCurrentPage(0);
		center.addSouth(navigation, 100);
		center.add(new ContentBlock());
		blockHandler.add(center);

	}
}
