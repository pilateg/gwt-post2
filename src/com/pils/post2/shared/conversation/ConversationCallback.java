package com.pils.post2.shared.conversation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pils.post2.client.ClientUtils;

public abstract class ConversationCallback<T> implements AsyncCallback<T> {
	@Override
	public void onFailure(Throwable caught) {
		ClientUtils.log(caught);
	}
}
