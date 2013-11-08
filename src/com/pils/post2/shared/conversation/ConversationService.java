package com.pils.post2.shared.conversation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pils.post2.shared.dto.Comment;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.SessionUser;
import com.pils.post2.shared.dto.User;

@RemoteServiceRelativePath("Post2Service")
public interface ConversationService extends RemoteService {

	SessionUser login(String name, String password);

	User getUser(long sessionId);

	boolean logout(long sessionId);

	boolean addComment(long sessionId, Comment comment);

	boolean addEntry(long sessionId, Entry entry);
}
