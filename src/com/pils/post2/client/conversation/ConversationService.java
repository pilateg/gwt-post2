package com.pils.post2.client.conversation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pils.post2.Tuple2;
import com.pils.post2.client.conversation.dto.User;

@RemoteServiceRelativePath("Post2Service")
public interface ConversationService extends RemoteService {

	Tuple2<Long, User> login(String name, String password);

	User getUser(long sessionId);

	boolean logout(long sessionId);
}
