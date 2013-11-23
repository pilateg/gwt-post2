package com.pils.post2.client.uiblocks;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.pils.post2.client.ClientUtils;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Comment;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.User;
import com.pils.post2.client.layout.widgets.Button;

import java.util.Date;
import java.util.List;

public class DiscussionBlock extends Composite {

	private FlowPanel mainPanel;
	private TextArea textArea;
	private Button addComment;

	public DiscussionBlock(final Entry entry) {
		mainPanel = new FlowPanel();
		addComment = new Button("add comment");
		addComment.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				User user = ConversationManager.getCurrentUser();
				if (user != null && !ClientUtils.isEmpty(textArea.getText())) {
					addComment.setText("adding...");
					addComment.setEnabled(false);
					final Comment comment = new Comment();
					comment.setAuthor(user);
					comment.setContent(ClientUtils.escape(textArea.getText()));
					comment.setDate(new Date());
					comment.setEntry(entry);
					ConversationManager.addComment(comment, new ConversationCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								mainPanel.add(new CommentBlock(comment));
								textArea.setText("");
							}
							addComment.setEnabled(true);
							addComment.setText("add comment");
						}
					});
				}
			}
		});
		textArea = new TextArea();
		initAddComment();
		initWidget(mainPanel);
	}

	public void setComments(List<Comment> comments) {
		mainPanel.clear();
		initAddComment();
		if (comments != null)
			for (Comment c : comments)
				mainPanel.add(new CommentBlock(c));
	}

	private void initAddComment() {
		if (ConversationManager.getCurrentUser() != null) {
			mainPanel.add(textArea);
			mainPanel.add(addComment);
		}
	}
}
