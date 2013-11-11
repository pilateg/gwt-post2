package com.pils.post2.client.uiblocks;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.ClientUtils;
import com.pils.post2.client.NavigationMediator;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Entity;

import java.util.ArrayList;
import java.util.List;

public class SearchBlock extends Composite {

	public SearchBlock() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		final TextBox textBox = new TextBox();
		final SuggestBox text = new SuggestBox(new SuggestOracle() {
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				ConversationManager.lightSearch(textBox.getText(), new ConversationCallback<List<? extends Entity>>() {
					@Override
					public void onSuccess(List<? extends Entity> result) {
						List<Suggestion> suggestions = new ArrayList<Suggestion>();
						for (Entity entity : result)
							suggestions.add(new EntitySuggestion(null, ClientUtils.trim(entity.getTitle(), 50)));
						callback.onSuggestionsReady(request, new Response(suggestions));
					}
				});
			}
		}, textBox);
		text.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				NavigationMediator.getEntitySelectionHandler().onEntitySelected(
						((EntitySuggestion) event.getSelectedItem()).entity);
			}
		});
		text.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER)
					ConversationManager.search(textBox.getText(), new ConversationCallback<List<? extends Entity>>() {
						@Override
						public void onSuccess(List<? extends Entity> result) {
							NavigationMediator.getBreadcrumbBlock().clear();
							NavigationMediator.getContentBlock().setEntries(result);
							NavigationMediator.getNavigationBlock().setVisible(true);
							//NavigationMediator.getNavigationBlock().setUp(result.size(), ConversationManager.getItemsOnPage());
							NavigationMediator.getNavigationBlock().setCurrentPage(0);
						}
					});
			}
		});
		text.getElement().setAttribute("placeholder", "search");
		mainPanel.add(text);
		initWidget(mainPanel);
	}

	private class EntitySuggestion extends MultiWordSuggestOracle.MultiWordSuggestion {

		Entity entity;

		private EntitySuggestion() {}

		private EntitySuggestion(String replacementString, String displayString) {
			super(replacementString, displayString);
		}
	}
}
