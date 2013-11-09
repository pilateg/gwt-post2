package com.pils.post2.client;

import com.pils.post2.shared.dto.Comment;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.client.uiblocks.ContentBlock;
import com.pils.post2.client.uiblocks.EntityBlock;
import com.pils.post2.client.uiblocks.NavigationBlock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NavigationMediator {

	private static ContentBlock contentBlock;

	private NavigationMediator() {
	}

	public static void init(ContentBlock content) {
		contentBlock = content;
	}

	public static NavigationBlock.PageSelectionHandler getPageSelectionHandler() {
		final int itemsNumber = 20;
		final List<Entity> entries = new ArrayList<Entity>(itemsNumber);
		for (int i = 0; i < itemsNumber; ++i) {
			Entry entry = new Entry();
			entry.setTitle("entry" + i);
			entry.setContent("<b>" + i + "</b>");
			final Comment comment = new Comment();
			comment.setDate(new Date());
			comment.setContent("blabla" + i);
			entry.setComments(new ArrayList<Comment>(){{add(comment);}});
			entries.add(entry);
		}

		return new NavigationBlock.PageSelectionHandler() {
			@Override
			public void onPageSelected(int pageNumber, int itemsOnPage) {
				if (pageNumber == -1) {
					contentBlock.setEntries(null);
					return;
				}
				int from = pageNumber * itemsOnPage;
				int to = from + itemsOnPage > itemsNumber ? itemsNumber : from + itemsOnPage;
				contentBlock.setEntries(entries.subList(from, to));
			}
		};
	}

	public static EntityBlock.EntitySelectionHandler getEntitySelectionHandler() {
		return new EntityBlock.EntitySelectionHandler() {
			@Override
			public void onEntitySelected(Entity e) {
				//update breadcrumb, navigation
				contentBlock.setEntry(e);
			}
		};
	}
}
