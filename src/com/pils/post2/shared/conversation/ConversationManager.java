package com.pils.post2.shared.conversation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pils.post2.shared.dto.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ConversationManager {

	private static final long EXPIRE_PERIOD = 1000 * 60 * 60 * 24 * 30L;
	private static final String COOKIE_NAME = "sid";
	private static final ConversationServiceAsync SERVICE = GWT.create(ConversationService.class);

	private static long sessionId = -1;
	private static User currentUser;

	private static List<AsyncCallback<User>> loginCallbacks = new LinkedList<AsyncCallback<User>>();
	private static List<AsyncCallback<Boolean>> logoutCallbacks = new LinkedList<AsyncCallback<Boolean>>();

	private ConversationManager() {
	}

	public static void restoreSession() {
		long sid;
		try {
			sid = Long.parseLong(Cookies.getCookie(COOKIE_NAME));
		} catch (NumberFormatException e) {
			sid = -1;
		}
		final long finalSid = sid;
		if (sid != -1)
			SERVICE.getUser(sid, new ConversationCallback<User>() {
				@Override
				public void onSuccess(User user) {
					if (user != null) {
						sessionId = finalSid;
						currentUser = user;
					} else
						Cookies.removeCookie(COOKIE_NAME, "/");
					onSuccessLoginCallbacks(user);
				}
			});
	}

	public static void login(String name, String password) {
		SERVICE.login(name, password, new ConversationCallback<SessionUser>() {
			@Override
			public void onSuccess(SessionUser sessionUser) {
				if (sessionUser != null) {
					sessionId = sessionUser.sessionId;
					currentUser = sessionUser.user;
					Date expires = new Date(System.currentTimeMillis() + EXPIRE_PERIOD);
					Cookies.setCookie(COOKIE_NAME, sessionUser.sessionId.toString(), expires, null, "/", false);
				} else {
					sessionId = -1;
					currentUser = null;
				}
				onSuccessLoginCallbacks(sessionUser == null ? null : sessionUser.user);
			}
		});
	}

	public static void logout() {
		if (sessionId != -1)
			SERVICE.logout(sessionId, new ConversationCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						sessionId = -1;
						currentUser = null;
						Cookies.removeCookie(COOKIE_NAME, "/");
					}
					onSuccessLogoutCallbacks(result);
				}
			});
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static int getItemsOnPage() {
		return 7;
	}

	public static void addComment(Comment comment, AsyncCallback<Boolean> callback) {
		SERVICE.addComment(sessionId, comment, callback);
	}

	public static void addEntry(Entry entry, AsyncCallback<Boolean> callback) {
		SERVICE.addEntry(sessionId, entry, callback);
	}

	public static void addSection(Section section, AsyncCallback<Boolean> callback) {
		SERVICE.addSection(sessionId, section, callback);
	}

	public static void fetchSections(AsyncCallback<List<Section>> callback) {
		SERVICE.fetchSections(sessionId, callback);
	}

	public static void fetchUsers(String query, AsyncCallback<List<User>> callback) {
		SERVICE.fetchUsers(sessionId, query, callback);
	}

	public static void lightSearch(String query, AsyncCallback<List<? extends Entity>> callback) {
		SERVICE.lightSearch(sessionId, query, callback);
	}

	public static void search(String query, long from, long number, AsyncCallback<EntitiesList> callback) {
		SERVICE.search(sessionId, query, from, number, callback);
	}

	public static void fetchEntities(Entity parent, int from, int number, AsyncCallback<EntitiesList> callback) {
		SERVICE.fetchEntities(sessionId, parent, from, number, callback);
	}

	public static void fetchEntry(long entryId, AsyncCallback<Entry> callback) {
		SERVICE.fetchEntry(sessionId, entryId, callback);
	}

	public static void addLoginCallback(AsyncCallback<User> callback) {
		loginCallbacks.add(callback);
	}

	public static void removeLoginCallback(AsyncCallback<User> callback) {
		if (loginCallbacks.contains(callback))
			loginCallbacks.remove(callback);
	}

	private static void onSuccessLoginCallbacks(User user) {
		if (!loginCallbacks.isEmpty())
			for (AsyncCallback<User> callback : loginCallbacks)
				if (callback != null)
					callback.onSuccess(user);
	}

	public static void addLogoutCallback(AsyncCallback<Boolean> callback) {
		logoutCallbacks.add(callback);
	}

	public static void removeLogoutCallback(AsyncCallback<Boolean> callback) {
		if (logoutCallbacks.contains(callback))
			logoutCallbacks.remove(callback);
	}

	private static void onSuccessLogoutCallbacks(Boolean result) {
		if (!logoutCallbacks.isEmpty())
			for (AsyncCallback<Boolean> callback : logoutCallbacks)
				if (callback != null)
					callback.onSuccess(result);
	}
}
