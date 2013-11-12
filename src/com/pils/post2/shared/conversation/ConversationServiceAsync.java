package com.pils.post2.shared.conversation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pils.post2.shared.dto.*;

import java.util.List;

public interface ConversationServiceAsync {
	void login(String name, String password, AsyncCallback<SessionUser> async);

	void getUser(long sessionId, AsyncCallback<User> async);

	void logout(long sessionId, AsyncCallback<Boolean> async);

	void addComment(long sessionId, Comment comment, AsyncCallback<Boolean> async);

	void addEntry(long sessionId, Entry entry, AsyncCallback<Boolean> async);

	void addSection(long sessionId, Section section, AsyncCallback<Boolean> async);

	void fetchSections(long sessionId, AsyncCallback<List<Section>> async);

	void fetchUsers(long sessionId, String query, AsyncCallback<List<User>> async);

	void lightSearch(long sessionId, String query, long from, long number, AsyncCallback<List<? extends Entity>> async);

	void search(long sessionId, String query, long from, long number, AsyncCallback<List<? extends Entity>> async);

	void fetchEntities(long sessionId, Entity parent, int from, int number, AsyncCallback<EntitiesList> async);
}
