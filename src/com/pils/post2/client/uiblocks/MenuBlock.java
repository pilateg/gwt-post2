package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.ClientUtils;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.*;

import java.util.ArrayList;
import java.util.List;

public class MenuBlock extends Composite {
	interface MenuBlockUiBinder extends UiBinder<FlowPanel, MenuBlock> {}
	private static MenuBlockUiBinder uiBinder = GWT.create(MenuBlockUiBinder.class);

	public static final MenuBlock INSTANCE = new MenuBlock(MainBlock.INSTANCE);

	@UiField protected FlowPanel loginPanel;
	@UiField protected TextBox nameText;
	@UiField protected TextBox passText;
	@UiField protected Button loginButton;
	@UiField protected Button registerButton;
	@UiField protected FlowPanel logoutPanel;
	@UiField protected Label userNameLabel;
	@UiField protected Button logoutButton;
	protected TextBox suggestText = new TextBox();
	@UiField(provided = true) protected SuggestBox searchSuggest;
	@UiField protected Button searchButton;
	@UiField protected Label sectionsLabel;
	protected PopupBlock addSectionPopup = new PopupBlock();
	@UiField protected Button addSectionButton;
	@UiField protected FlowPanel sectionsPanel;

	protected MainBlock mainBlock;
	protected Entity currentEntity;
	protected String currentSearchQuery;

	private MenuBlock(MainBlock mainBlock) {
		this.mainBlock = mainBlock;
		initAuthenticationCallbacks();
		initSearchBlock();
		initSectionsBlock();

		initWidget(uiBinder.createAndBindUi(this));

		nameText.getElement().setAttribute("placeholder", "name");
		passText.getElement().setAttribute("placeholder", "pass");
		searchSuggest.getElement().setAttribute("placeholder", "search");
	}

	private void initAuthenticationCallbacks() {
		ConversationManager.addLoginCallback(new ConversationCallback<User>() {
			@Override
			public void onSuccess(User user) {
				setUser(user);
			}
		});
		ConversationManager.addLogoutCallback(new ConversationCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					setUser(null);
					mainBlock.breadcrumbPanel.clear();
				}
			}
		});
	}

	private void initSearchBlock() {
		searchSuggest = new SuggestBox(new SuggestOracle() {
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				ConversationManager.lightSearch(suggestText.getText(),
						new ConversationCallback<List<? extends Entity>>() {
							@Override
							public void onSuccess(List<? extends Entity> result) {
								List<Suggestion> suggestions = new ArrayList<Suggestion>();
								if (result != null)
									for (Entity entity : result)
										suggestions.add(new EntitySuggestion(null, ClientUtils.trim(entity.getTitle(), 50)));
								callback.onSuggestionsReady(request, new Response(suggestions));
							}
						});
			}
		}, suggestText);
		searchSuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				onEntitySelected(((EntitySuggestion) event.getSelectedItem()).entity);
			}
		});
	}

	private void initSectionsBlock() {
		final TextBox sectionTitle = new TextBox();
		addSectionPopup.addWidget(sectionTitle);
		final CheckBox checkBox = new CheckBox("open for all");
		addSectionPopup.addWidget(checkBox);
		final List<User> users = new ArrayList<User>();
		final TextBox accessUsersTextBox = new TextBox();
		final SuggestBox accessUsers = new SuggestBox(new SuggestOracle() {
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				ConversationManager.fetchUsers(accessUsersTextBox.getText(), new ConversationCallback<List<User>>() {
					@Override
					public void onSuccess(List<User> result) {
						List<Suggestion> suggestions = new ArrayList<Suggestion>();
						for (User user : result)
							suggestions.add(new MultiWordSuggestOracle.MultiWordSuggestion(user.getName() + ", ", user.getName()));
						callback.onSuggestionsReady(request, new Response(suggestions));
					}
				});
			}
		}, accessUsersTextBox);
		accessUsers.setVisible(false);
		addSectionPopup.addWidget(accessUsers);
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				accessUsers.setVisible(event.getValue());
			}
		});
		addSectionPopup.setButtons("create section", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (sectionTitle.getText() == null || sectionTitle.getText().isEmpty())
							return;
						final Section section = new Section();
						section.setTitle(sectionTitle.getText());
						section.setOwner(ConversationManager.getCurrentUser());
						section.setOpenForAll(checkBox.getValue());
						if (section.isOpenForAll())
							section.setUsersWithAccess(users);
						ConversationManager.addSection(section, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									addSectionPopup.hide();
									sectionTitle.setText("");
									//add section to db and update
									sectionsPanel.add(new EntityLinkBlock(section));
								}
							}
						});
					}
				},
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent cancelEvent) {
						addSectionPopup.hide();
						sectionTitle.setText("");
					}
				}
		);
	}

	private void setUser(User user) {
		if (user != null) {
			userNameLabel.setText(user.getName());
			loginPanel.setVisible(false);
			logoutPanel.setVisible(true);

			sectionsLabel.setText("my sections");
			addSectionButton.setVisible(true);
		} else {
			nameText.setText("");
			passText.setText("");
			loginPanel.setVisible(true);
			logoutPanel.setVisible(false);

			sectionsLabel.setText("links");
			addSectionButton.setVisible(false);
		}
		ConversationManager.fetchSections(new ConversationCallback<List<Section>>() {
			@Override
			public void onSuccess(List<Section> sections) {
				sectionsPanel.clear();
				if (sections != null)
					for (Section section : sections)
						sectionsPanel.add(new EntityLinkBlock(section));
			}
		});
	}

	public void onEntitySelected(Entity e) {
		currentEntity = e;
		currentSearchQuery = null;
		mainBlock.setBreadcrumb(e);
		switch (e.getType()) {
			case Comment:
				mainBlock.setEntry(e);
				mainBlock.navigationPanel.setVisible(false);
				break;
			case Entry:
				ConversationManager.fetchEntry(e.getId(), new ConversationCallback<Entry>() {
					@Override
					public void onSuccess(Entry result) {
						mainBlock.navigationPanel.setVisible(false);
						mainBlock.setEntry(result);
					}
				});
				break;
			case Section:
				mainBlock.setCurrentPage(0);
				break;
			case Tag:
				//get entities by tag
				break;
			case User:
				//show user card
				break;
		}
		//update navigation
	}

	@UiHandler(value={"nameText", "passText"})
	void nameKeyPress(KeyPressEvent e) {
		if (e.getCharCode() == KeyCodes.KEY_ENTER)
			ConversationManager.login(nameText.getText(), passText.getText());
	}

	@UiHandler("loginButton")
	void loginClick(ClickEvent e) {
		ConversationManager.login(nameText.getText(), passText.getText());
	}

	@UiHandler("registerButton")
	void registerClick(ClickEvent e) {
	}

	@UiHandler("logoutButton")
	void logoutClick(ClickEvent e) {
		ConversationManager.logout();
	}

	@UiHandler("searchSuggest")
	void searchKeyPress(KeyPressEvent e) {
		if (e.getCharCode() == KeyCodes.KEY_ENTER) {
			currentSearchQuery = suggestText.getText();
			currentEntity = null;
			mainBlock.setCurrentPage(0);
		}
	}

	@UiHandler("searchButton")
	void searchClick(ClickEvent e) {
		currentSearchQuery = suggestText.getText();
		currentEntity = null;
		mainBlock.setCurrentPage(0);
	}

	@UiHandler("addSectionButton")
	void addSectionClick(ClickEvent e) {
		if (ConversationManager.getCurrentUser() != null)
			addSectionPopup.center();
	}

	private static class EntitySuggestion extends MultiWordSuggestOracle.MultiWordSuggestion {

		Entity entity;

		private EntitySuggestion() {}

		private EntitySuggestion(String replacementString, String displayString) {
			super(replacementString, displayString);
		}
	}
}
