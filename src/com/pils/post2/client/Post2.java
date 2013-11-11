package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.uiblocks.*;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Section;
import com.pils.post2.shared.dto.User;

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
		NavigationMediator.init(new LoginBlock(), new ContentBlock(), new LinksBlock());
		NavigationMediator.addLoginCallback(new ConversationCallback<User>() {
			@Override
			public void onSuccess(User user) {
				NavigationMediator.getLoginBlock().setUser(user);
				NavigationMediator.getSectionsBlock().setUser(user);
			}
		});
		NavigationMediator.addLogoutCallback(new ConversationCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					NavigationMediator.getLoginBlock().setUser(null);
					NavigationMediator.getSectionsBlock().setUser(null);
				}
			}
		});
		ConversationManager.restoreSession();
		Resources.INSTANCE.css().ensureInjected();
		init();
	}

	public static void init() {
		final DockLayoutPanel blockHandler = new DockLayoutPanel(Style.Unit.PX);
		RootLayoutPanel.get().add(blockHandler);

		VerticalPanel east = new VerticalPanel();
		east.addStyleName(Resources.INSTANCE.css().sidePanel());
		east.add(NavigationMediator.getLoginBlock());
		east.add(new SearchBlock());
		final List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < 4; ++i) {
			Section section = new Section();
			section.setTitle("tag_name" + i);
			entities.add(section);
		}
		if (ConversationManager.getCurrentUser() == null)
			NavigationMediator.getSectionsBlock().setUser(null);
		east.add(NavigationMediator.getSectionsBlock());
		blockHandler.addEast(east, 200);
		final DockLayoutPanel center = new DockLayoutPanel(Style.Unit.PX);
		BreadcrumbBlock breadcrumb = new BreadcrumbBlock();
		breadcrumb.addBreadcrumb(entities.get(0));
		breadcrumb.addBreadcrumb(entities.get(1));
		center.addNorth(breadcrumb, 50);
		final NavigationBlock navigation = new NavigationBlock();
		final int itemsNumber = 20;
		navigation.setUp(itemsNumber, 7);
		center.addSouth(navigation, 100);
		navigation.setPageSelectionHandler(NavigationMediator.getPageSelectionHandler());
		navigation.setCurrentPage(0);
		center.add(NavigationMediator.getContentBlock());
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
