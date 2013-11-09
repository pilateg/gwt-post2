package com.pils.post2.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pils.post2.client.uiblocks.*;
import com.pils.post2.shared.dto.Comment;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NavigationMediator {

	private static LoginBlock loginBlock;
	private static ContentBlock contentBlock;
	private static LinksBlock sectionsBlock;
	private static List<AsyncCallback<User>> loginCallbacks = new LinkedList<AsyncCallback<User>>();
	private static List<AsyncCallback<Boolean>> logoutCallbacks = new LinkedList<AsyncCallback<Boolean>>();

	private NavigationMediator() {
	}

	public static void init(LoginBlock login, ContentBlock content, LinksBlock links) {
		loginBlock = login;
		contentBlock = content;
		sectionsBlock = links;
	}

	public static NavigationBlock.PageSelectionHandler getPageSelectionHandler() {
		final int itemsNumber = 20;
		final List<Entity> entries = new ArrayList<Entity>(itemsNumber);
		for (int i = 0; i < itemsNumber; ++i) {
			Entry entry = new Entry();
			entry.setTitle("entry" + i);
			entry.setContent("<b>" + i + "</b>");
			final Comment comment = new Comment();
			comment.setDate(new Date());
			comment.setContent("blabla" + i);
			entry.setComments(new ArrayList<Comment>(){{add(comment);}});
			entries.add(entry);
		}

		return new NavigationBlock.PageSelectionHandler() {
			@Override
			public void onPageSelected(int pageNumber, int itemsOnPage) {
				if (pageNumber == -1) {
					contentBlock.setEntries(null);
					return;
				}
				int from = pageNumber * itemsOnPage;
				int to = from + itemsOnPage > itemsNumber ? itemsNumber : from + itemsOnPage;
				contentBlock.setEntries(entries.subList(from, to));
			}
		};
	}

	public static EntityBlock.EntitySelectionHandler getEntitySelectionHandler() {
		return new EntityBlock.EntitySelectionHandler() {
			@Override
			public void onEntitySelected(Entity e) {
				//update breadcrumb, navigation
				contentBlock.setEntry(e);
			}
		};
	}

	public static void addLoginCallback(AsyncCallback<User> callback) {
		loginCallbacks.add(callback);
	}

	public static void removeLoginCallback(AsyncCallback<User> callback) {
		if (loginCallbacks.contains(callback))
			loginCallbacks.remove(callback);
	}

	public static void onSuccessLoginCallbacks(User user) {
		if (!getLoginCallbacks().isEmpty())
			for (AsyncCallback<User> callback : getLoginCallbacks())
				if (callback != null)
					callback.onSuccess(user);
	}

	public static List<AsyncCallback<User>> getLoginCallbacks() {
		return loginCallbacks;
	}

	public static void addLogoutCallback(AsyncCallback<Boolean> callback) {
		logoutCallbacks.add(callback);
	}

	public static void removeLogoutCallback(AsyncCallback<Boolean> callback) {
		if (logoutCallbacks.contains(callback))
			logoutCallbacks.remove(callback);
	}

	public static void onSuccessLogoutCallbacks(Boolean result) {
		if (!getLogoutCallbacks().isEmpty())
			for (AsyncCallback<Boolean> callback : getLogoutCallbacks())
				if (callback != null)
					callback.onSuccess(result);
	}

	public static List<AsyncCallback<Boolean>> getLogoutCallbacks() {
		return logoutCallbacks;
	}

	public static LoginBlock getLoginBlock() {
		return loginBlock;
	}

	public static ContentBlock getContentBlock() {
		return contentBlock;
	}

	public static LinksBlock getSectionsBlock() {
		return sectionsBlock;
	}
}
