package com.pils.post2.client.uiblocks;

import com.pils.post2.shared.dto.Comment;

public class CommentBlock extends EntityBlock {

	public CommentBlock(Comment e) {
		super(e);
		StringBuilder sb = new StringBuilder();
		if (e.getDate() != null)
			sb.append(e.getDate().toString()).append(" ");
		if (e.getAuthor() != null)
			sb.append(e.getAuthor().getName());
		sb.append(":");
		link.setText(sb.toString());
		description.setText(e.getContent());
	}
}
