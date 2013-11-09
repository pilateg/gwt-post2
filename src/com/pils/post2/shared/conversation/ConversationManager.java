package com.pils.post2.shared.conversation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pils.post2.client.NavigationMediator;
import com.pils.post2.shared.dto.*;

import java.util.Date;

public class ConversationManager {

	private static final long EXPIRE_PERIOD = 1000 * 60 * 60 * 24 * 30L;
	private static final String COOKIE_NAME = "sid";
	private static final ConversationServiceAsync SERVICE = GWT.create(ConversationService.class);

	private static long sessionId = -1;
	private static User currentUser;

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
					NavigationMediator.onSuccessLoginCallbacks(user);
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
				NavigationMediator.onSuccessLoginCallbacks(sessionUser == null ? null : sessionUser.user);
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
					NavigationMediator.onSuccessLogoutCallbacks(result);
				}
			});
	}

	public static User getCurrentUser() {
		return currentUser;
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
}
