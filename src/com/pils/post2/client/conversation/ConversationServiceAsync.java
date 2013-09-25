package com.pils.post2.client.conversation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConversationServiceAsync {
	void getMessage(String msg, AsyncCallback<String> async);
}
