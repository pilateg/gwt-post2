package com.pils.post2.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Post2Service")
public interface ConversationService extends RemoteService {
	// Sample interface method of remote interface
	String getMessage(String msg);

	/**
	 * Utility/Convenience class.
	 * Use ConversationService.App.getInstance() to access static instance of Post2ServiceAsync
	 */
	public static class App {
		private static ConversationServiceAsync ourInstance = GWT.create(ConversationService.class);

		public static synchronized ConversationServiceAsync getInstance() {
			return ourInstance;
		}
	}
}
