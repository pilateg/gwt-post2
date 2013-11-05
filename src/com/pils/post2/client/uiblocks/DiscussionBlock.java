package com.pils.post2.client.uiblocks;

import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.conversation.dto.Comment;
import com.pils.post2.client.layout.UiBlock;

import java.util.List;

public class DiscussionBlock extends UiBlock {

	private FlowPanel mainPanel;

	public DiscussionBlock() {
		mainPanel = new FlowPanel();
		initWidget(mainPanel);
	}

	public void setComments(List<Comment> comments) {
		mainPanel.clear();
		if (comments != null)
			for (Comment c : comments)
				mainPanel.add(new CommentBlock(c));
	}
}
