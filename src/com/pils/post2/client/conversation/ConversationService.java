package com.pils.post2.client.conversation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Post2Service")
public interface ConversationService extends RemoteService {
	String getMessage(String msg);

	public static class App {
		private static ConversationServiceAsync ourInstance = GWT.create(ConversationService.class);

		public static synchronized ConversationServiceAsync getInstance() {
			return ourInstance;
		}
	}
}
