package com.pils.post2.client.conversation.dto;

import java.io.Serializable;

public class SessionUser implements Serializable {
	public Long sessionId;
	public User user;

	public SessionUser() {
	}

	public SessionUser(long sessionId, User user) {
		this.sessionId = sessionId;
		this.user = user;
	}
}
