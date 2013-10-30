package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.conversation.dto.Tag;
import com.pils.post2.client.uiblocks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Post2 implements EntryPoint {
	private Logger logger = Logger.getLogger("");

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				logger.log(Level.SEVERE, "!!:", unwrap(e));
			}
		});
		DockLayoutPanel blockHandler = new DockLayoutPanel(Style.Unit.PX);
		RootLayoutPanel.get().add(blockHandler);

		DockLayoutPanel east = new DockLayoutPanel(Style.Unit.PX);
		east.addNorth(new LoginBlock(), 200);
		east.addNorth(new SearchBlock(), 200);
		Tag tag = new Tag();
		final List<EntityLinkBlock> entities = new ArrayList<EntityLinkBlock>();
		for (int i = 0; i < 4; ++i) {
			EntityLinkBlock entityLink = new EntityLinkBlock();
			tag.setName("tag_name" + i);
			entityLink.setEntity(tag);
			entities.add(entityLink);
		}
		CategoriesBlock categoriesBlock = new CategoriesBlock();
		categoriesBlock.setCategories(new ArrayList<EntityLinkBlock>(){{add(entities.get(2)); add(entities.get(3));}});
		east.addNorth(categoriesBlock, 200);
		blockHandler.addEast(east, 200);
		DockLayoutPanel center = new DockLayoutPanel(Style.Unit.PX);
		BreadcrumbBlock breadcrumb = new BreadcrumbBlock();
		breadcrumb.addBreadcrumb(entities.get(0));
		breadcrumb.addBreadcrumb(entities.get(1));
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

	public static Throwable unwrap(Throwable e) {
		if (e instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) e;
			if (ue.getCauses().size() == 1) {
				return unwrap(ue.getCauses().iterator().next());
			}
		}
		return e;
	}
}
