package com.pils.post2.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConversationServiceAsync {
	void getMessage(String msg, AsyncCallback<String> async);
}
