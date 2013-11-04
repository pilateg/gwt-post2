package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.conversation.dto.Entry;
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
		final DockLayoutPanel blockHandler = new DockLayoutPanel(Style.Unit.PX);
		RootLayoutPanel.get().add(blockHandler);

		DockLayoutPanel east = new DockLayoutPanel(Style.Unit.PX);
		east.addNorth(new LoginBlock(), 200);
		east.addNorth(new SearchBlock(), 200);
		Tag tag = new Tag();
		final List<EntityLinkBlock> entities = new ArrayList<EntityLinkBlock>();
		for (int i = 0; i < 4; ++i) {
			tag.setTitle("tag_name" + i);
			EntityLinkBlock entityLink = new EntityLinkBlock(tag);
			entities.add(entityLink);
		}
		CategoriesBlock categoriesBlock = new CategoriesBlock();
		categoriesBlock.setCategories(entities.subList(2,4));
		east.addNorth(categoriesBlock, 200);
		blockHandler.addEast(east, 200);
		final DockLayoutPanel center = new DockLayoutPanel(Style.Unit.PX);
		BreadcrumbBlock breadcrumb = new BreadcrumbBlock();
		breadcrumb.addBreadcrumb(entities.get(0));
		breadcrumb.addBreadcrumb(entities.get(1));
		center.addNorth(breadcrumb, 40);
		final NavigationBlock navigation = new NavigationBlock();
		final int itemsNumber = 20;
		navigation.setUp(itemsNumber, 7);
		center.addSouth(navigation, 100);
		Entry entry = new Entry();
		final List<EntryBlock> entries = new ArrayList<EntryBlock>(itemsNumber);
		for (int i = 0; i < itemsNumber; ++i) {
			entry.setTitle("entry"+i);
			entry.setContent("<b>"+i+"</b>");
			EntryBlock entryBlock = new EntryBlock(entry);
			entries.add(entryBlock);
		}
		final ContentBlock contentBlock = new ContentBlock();
		navigation.setPageSelectionHandler(new NavigationBlock.PageSelectionHandler() {
			@Override
			public void onPageSelected(int pageNumber, int itemsOnPage) {
				if (pageNumber == -1) {
					contentBlock.setEntries(null);
					return;
				}
				int from = pageNumber * itemsOnPage;
				int to = from + itemsOnPage > itemsNumber + 1 ? itemsNumber + 1 : from + itemsOnPage;
				contentBlock.setEntries(entries.subList(from, to));
			}
		});
		navigation.setCurrentPage(0);
		center.add(contentBlock);
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
