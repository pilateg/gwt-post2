package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.conversation.dto.Entity;
import com.pils.post2.client.conversation.dto.Entry;
import com.pils.post2.client.conversation.dto.Section;
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
		final ContentBlock contentBlock = new ContentBlock();
		NavigationMediator.init(contentBlock);

		DockLayoutPanel east = new DockLayoutPanel(Style.Unit.PX);
		east.addNorth(new LoginBlock(), 200);
		east.addNorth(new SearchBlock(), 200);
		final List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < 4; ++i) {
			Section section = new Section();
			section.setTitle("tag_name" + i);
			entities.add(section);
		}
		LinksBlock linksBlock = new LinksBlock("links");
		linksBlock.setCategories(entities.subList(2, 4));
		east.addNorth(linksBlock, 200);
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
		navigation.setPageSelectionHandler(NavigationMediator.getPageSelectionHandler());
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
