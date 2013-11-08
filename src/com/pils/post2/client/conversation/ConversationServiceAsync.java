package com.pils.post2.client.conversation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pils.post2.client.conversation.dto.Comment;
import com.pils.post2.client.conversation.dto.Entry;
import com.pils.post2.client.conversation.dto.SessionUser;
import com.pils.post2.client.conversation.dto.User;

public interface ConversationServiceAsync {
	void login(String name, String password, AsyncCallback<SessionUser> async);

	void getUser(long sessionId, AsyncCallback<User> async);

	void logout(long sessionId, AsyncCallback<Boolean> async);

	void addComment(long sessionId, Comment comment, AsyncCallback<Boolean> async);

	void addEntry(long sessionId, Entry entry, AsyncCallback<Boolean> async);
}
