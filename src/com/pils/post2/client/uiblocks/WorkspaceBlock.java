package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
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

public class WorkspaceBlock extends Composite {
	interface WorkspaceBlockUiBinder extends UiBinder<DockLayoutPanel, WorkspaceBlock> {}
	private static WorkspaceBlockUiBinder uiBinder = GWT.create(WorkspaceBlockUiBinder.class);

	public static final WorkspaceBlock INSTANCE = new WorkspaceBlock();

	@UiField protected FlowPanel loginPanel;
	@UiField protected TextBox nameText;
	@UiField protected TextBox passText;
	@UiField protected Button loginButton;
	protected PopupBlock addUserPopup = new PopupBlock();
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
	@UiField protected FlowPanel breadcrumbPanel;
	protected PopupBlock addEntryPopup = new PopupBlock();
	@UiField protected Button addEntryButton;
	@UiField protected FlowPanel contentPanel;
	@UiField protected HorizontalPanel navigationBlock;
	@UiField protected Button firstButton;
	@UiField protected Button previousButton;
	@UiField protected Button nextButton;
	@UiField protected Button lastButton;
	@UiField protected FlowPanel navigationPanel;


	protected Entity currentEntity;
	protected String currentSearchQuery;
	private int currentPage = -1;
	private int itemsNumber = -1;
	private int itemsOnPage = -1;
	private int pagesNumber;

	private WorkspaceBlock() {
		initAuthenticationCallbacks();
		initLoginBlock();
		initSearchBlock();
		initSectionsBlock();
		initContentBlock();

		initWidget(uiBinder.createAndBindUi(this));

		navigationBlock.setCellWidth(firstButton, "30px");
		navigationBlock.setCellWidth(previousButton, "30px");
		navigationBlock.setCellWidth(nextButton, "30px");
		navigationBlock.setCellWidth(lastButton, "30px");
		nameText.getElement().setAttribute("placeholder", "name");
		passText.getElement().setAttribute("placeholder", "pass");
		searchSuggest.getElement().setAttribute("placeholder", "search");
		updateNavigation(0, ConversationManager.getItemsOnPage(), 0);
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
					setBreadcrumb(null);
					setEntry(null);
					updateNavigation(0, -1, 0);
				}
			}
		});
	}

	private void initLoginBlock() {
		final TextBox nameTextBox = new TextBox();
		final TextBox passwordTextBox = new TextBox();
		final TextBox password2TextBox = new TextBox();
		addUserPopup.addWidget(new Label("user name"), nameTextBox);
		addUserPopup.addWidget(new Label("password"), passwordTextBox);
		addUserPopup.addWidget(new Label("password"), password2TextBox);
		addUserPopup.setButtons("create user", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (ClientUtils.isEmpty(nameTextBox.getText()) ||
								ClientUtils.isEmpty(passwordTextBox.getText()) ||
								ClientUtils.isEmpty(password2TextBox.getText()) ||
								!passwordTextBox.getText().equals(password2TextBox.getText()))
							return;
						final User user = new User();
						user.setName(nameTextBox.getText());
						user.setPassword(passwordTextBox.getText());
						ConversationManager.addUser(user, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									addUserPopup.hide();
									ConversationManager.login(user.getName(), user.getPassword());
									nameTextBox.setText("");
									passwordTextBox.setText("");
									password2TextBox.setText("");
								}
							}
						});
					}
				},
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent cancelEvent) {
						addUserPopup.hide();
						nameTextBox.setText("");
						passwordTextBox.setText("");
						password2TextBox.setText("");
					}
				}
		);
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
										suggestions.add(new EntitySuggestion(entity, null,
												ClientUtils.trim(entity.getTitle(), 50)));
								callback.onSuggestionsReady(request, new Response(suggestions));
							}
						});
			}
		}, suggestText);
		searchSuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				selectEntity(((EntitySuggestion) event.getSelectedItem()).entity);
			}
		});
	}

	private void initSectionsBlock() {
		final TextBox sectionTitleTextBox = new TextBox();
		addSectionPopup.addWidget(new Label("section title"), sectionTitleTextBox);
		final CheckBox openForAllCheckBox = new CheckBox();
		addSectionPopup.addWidget(new Label("open for all"), openForAllCheckBox);
		final TextBox usersTextBox = new TextBox();
		final SuggestBox usersSuggest = new SuggestBox(new SuggestOracle() {
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				ConversationManager.fetchUsers(usersTextBox.getText(), new ConversationCallback<List<User>>() {
					@Override
					public void onSuccess(List<User> result) {
						List<Suggestion> suggestions = new ArrayList<Suggestion>();
						for (Entity entity : result)
							suggestions.add(new EntitySuggestion(entity, null,
									ClientUtils.trim(entity.getTitle(), 50)));
						callback.onSuggestionsReady(request, new Response(suggestions));
					}
				});
			}
		}, usersTextBox);
		final List<User> users = new ArrayList<User>();
		final Label usersLabel = new Label("users with access");
		final FlowPanel usersPanel = new FlowPanel();
		usersPanel.add(usersSuggest);
		usersSuggest.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent e) {
				if (e.getNativeKeyCode() == KeyCodes.KEY_ESCAPE)
					((SuggestBox.DefaultSuggestionDisplay) usersSuggest.getSuggestionDisplay()).hideSuggestions();
			}
		});
		usersSuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				final User user = (User) ((EntitySuggestion) event.getSelectedItem()).entity;
				users.add(user);
				final InlineEntityBlock inlineEntityBlock = new InlineEntityBlock(user);
				inlineEntityBlock.addCancelClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent e) {
						usersPanel.remove(inlineEntityBlock);
						users.remove(user);
						usersSuggest.setFocus(true);
					}
				});
				usersPanel.add(inlineEntityBlock);
				usersSuggest.setFocus(true);
			}
		});
		usersSuggest.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		addSectionPopup.addWidget(usersLabel, usersPanel);
		usersLabel.setVisible(false);
		usersSuggest.setVisible(false);
		openForAllCheckBox.setValue(true);
		openForAllCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				usersLabel.setVisible(!event.getValue());
				usersSuggest.setVisible(!event.getValue());
			}
		});
		addSectionPopup.setButtons("create section", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (ClientUtils.isEmpty(sectionTitleTextBox.getText()))
							return;
						final Section section = new Section();
						section.setTitle(sectionTitleTextBox.getText());
						section.setOwner(ConversationManager.getCurrentUser());
						section.setOpenForAll(openForAllCheckBox.getValue());
						if (section.isOpenForAll())
							section.setUsersWithAccess(users);
						ConversationManager.addSection(section, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									addSectionPopup.hide();
									sectionTitleTextBox.setText("");
									usersSuggest.setText("");
									usersPanel.clear();
									usersPanel.add(usersSuggest);
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
						sectionTitleTextBox.setText("");
						usersSuggest.setText("");
						usersPanel.clear();
						usersPanel.add(usersSuggest);
					}
				}
		);
	}

	private void initContentBlock() {
		final TextBox titleTextBox = new TextBox();
		addEntryPopup.addWidget(new Label("entry title"), titleTextBox);
		final TextArea contentText = new TextArea();
		addEntryPopup.addWidget(new Label("entry content"), contentText);
		final TextBox tagsTextBox = new TextBox();
		final SuggestBox tagsSuggest = new SuggestBox(new SuggestOracle() {
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				ConversationManager.fetchTags(tagsTextBox.getText(), new ConversationCallback<List<Tag>>() {
					@Override
					public void onSuccess(List<Tag> result) {
						List<Suggestion> suggestions = new ArrayList<Suggestion>();
						for (Entity entity : result)
							suggestions.add(new EntitySuggestion(entity, null,
									ClientUtils.trim(entity.getTitle(), 50)));
						callback.onSuggestionsReady(request, new Response(suggestions));
					}
				});
			}
		}, tagsTextBox);
		final List<Tag> tags = new ArrayList<Tag>();
		final FlowPanel tagsPanel = new FlowPanel();
		tagsPanel.add(tagsSuggest);
		tagsSuggest.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent e) {
				if (e.getNativeKeyCode() == KeyCodes.KEY_ESCAPE)
					((SuggestBox.DefaultSuggestionDisplay) tagsSuggest.getSuggestionDisplay()).hideSuggestions();
			}
		});
		tagsSuggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				final Tag tag = (Tag) ((EntitySuggestion) event.getSelectedItem()).entity;
				tags.add(tag);
				final InlineEntityBlock inlineEntityBlock = new InlineEntityBlock(tag);
				inlineEntityBlock.addCancelClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent e) {
						tagsPanel.remove(inlineEntityBlock);
						tags.remove(tag);
						tagsSuggest.setFocus(true);
					}
				});
				tagsPanel.add(inlineEntityBlock);
				tagsSuggest.setFocus(true);
			}
		});
		tagsSuggest.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		addEntryPopup.addWidget(new Label("tags"), tagsPanel);
		addEntryPopup.setButtons("create entry", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (ClientUtils.isEmpty(contentText.getText()))
							return;
						final Entry entry = new Entry();
						entry.setTitle(titleTextBox.getText());
						entry.setContent(contentText.getText());
						entry.setTags(tags);
						if (currentEntity instanceof Section)
							entry.setSection((Section) currentEntity);
						ConversationManager.addEntry(entry, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									addEntryPopup.hide();
									titleTextBox.setText("");
									contentText.setText("");
									tagsSuggest.setText("");
									tagsPanel.clear();
									tagsPanel.add(tagsSuggest);
									contentPanel.add(new EntryBlock(entry));
								}
							}
						});
					}
				},
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent cancelEvent) {
						addEntryPopup.hide();
						titleTextBox.setText("");
						contentText.setText("");
						tagsSuggest.setText("");
						tagsPanel.clear();
						tagsPanel.add(tagsSuggest);
					}
				}
		);
	}

	private void setUser(User user) {
		if (user != null) {
			userNameLabel.setText(user.getName());
			sectionsLabel.setText("my sections");
		} else {
			nameText.setText("");
			passText.setText("");
			sectionsLabel.setText("links");
			addEntryButton.setVisible(false);
		}
		loginPanel.setVisible(user == null);
		logoutPanel.setVisible(user != null);
		addSectionButton.setVisible(user != null);
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

	private void refreshContent() {
		//show loading on content panel
		final Entity entity = currentEntity;
		final int page = currentPage;
		final int pageSize = ConversationManager.getItemsOnPage();
		ConversationManager.fetchEntities(currentEntity, currentSearchQuery,
				currentPage * ConversationManager.getItemsOnPage(), ConversationManager.getItemsOnPage(),
				new ConversationCallback<EntitiesList>() {
					@Override
					public void onSuccess(EntitiesList result) {
						setBreadcrumb(entity);
						setEntries(result.entities);
						updateNavigation(result.number, pageSize, page);
						//hide loading on content panel
					}
				});
	}

	public void selectEntity(Entity e) {
		currentEntity = e;
		currentSearchQuery = null;
		switch (e.getType()) {
			case Comment:
			case User:
				setBreadcrumb(e);
				setEntry(e);
				updateNavigation(0, -1, 0);
				break;
			case Entry:
				ConversationManager.fetchEntry(e.getId(), new ConversationCallback<Entry>() {
					@Override
					public void onSuccess(Entry result) {
						setBreadcrumb(result);
						updateNavigation(0, -1, 0);
						setEntry(result);
					}
				});
				break;
			case Section:
			case Tag:
				currentPage = 0;
				refreshContent();
				break;
		}
	}

	protected void updateNavigation(int itemsNumber, int itemsOnPage, int page) {
		if ((itemsNumber != this.itemsNumber && itemsNumber != -1) ||
				(itemsOnPage != this.itemsOnPage && itemsOnPage != -1)) {
			if (itemsNumber != -1)
				this.itemsNumber = itemsNumber;
			if (itemsOnPage > 0)
				this.itemsOnPage = itemsOnPage;
			pagesNumber = this.itemsNumber / this.itemsOnPage + (this.itemsNumber % this.itemsOnPage == 0 ? 0 : 1);
			navigationBlock.setVisible(pagesNumber > 0);
			navigationPanel.clear();
			for (int i = 0; i < pagesNumber; ++i) {
				final Button button = new Button(String.valueOf(i + 1));
				button.getElement().getStyle().setProperty("display", "table-cell");
				final int finalI = i;
				button.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						currentPage = finalI;
						refreshContent();
					}
				});
				navigationPanel.add(button);
			}
		}
		for (int i = 0; i < navigationPanel.getWidgetCount(); i++)
			((Button) navigationPanel.getWidget(i)).setEnabled(true);
		try {
			((Button) navigationPanel.getWidget(page)).setEnabled(false);
		} catch (IndexOutOfBoundsException ignored) {
		}
		firstButton.setEnabled(page != 0);
		previousButton.setEnabled(page != 0);
		nextButton.setEnabled(page != pagesNumber - 1);
		lastButton.setEnabled(page != pagesNumber - 1);
	}

	protected void setBreadcrumb(Entity entity) {
		breadcrumbPanel.clear();
		if (entity == null)
			return;
		try {
			switch (entity.getType()) {
				case Comment:
					Comment comment = (Comment) entity;
					breadcrumbPanel.add(new EntityLinkBlock(comment.getEntry().getSection().getOwner()));
					breadcrumbPanel.add(new EntityLinkBlock(comment.getEntry().getSection()));
					breadcrumbPanel.add(new EntityLinkBlock(comment.getEntry()));
					break;
				case Entry:
					Entry entry = (Entry) entity;
					breadcrumbPanel.add(new EntityLinkBlock(entry.getSection().getOwner()));
					breadcrumbPanel.add(new EntityLinkBlock(entry.getSection()));
					breadcrumbPanel.add(new EntityLinkBlock(entry));
					break;
				case Section:
					Section section = (Section) entity;
					breadcrumbPanel.add(new EntityLinkBlock(section.getOwner()));
					breadcrumbPanel.add(new EntityLinkBlock(section));
					break;
				case Tag:
				case User:
					breadcrumbPanel.add(new EntityLinkBlock(entity));
					break;
			}
		} catch (Exception ignored) {}
	}

	protected void setEntries(List<? extends Entity> entities) {
		addEntryButton.setVisible(currentEntity != null && currentEntity.getType() == Entity.EntityType.Section);
		contentPanel.clear();
		if (entities != null)
			for (Entity entity : entities)
				contentPanel.add(new EntryBlock(entity));
	}

	protected void setEntry(Entity entity) {
		addEntryButton.setVisible(false);
		contentPanel.clear();
		if (entity != null)
			contentPanel.add(new EntryDetailedBlock(entity));
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
		if (ConversationManager.getCurrentUser() == null)
			addUserPopup.center();
	}

	@UiHandler("logoutButton")
	void logoutClick(ClickEvent e) {
		ConversationManager.logout();
	}

	@UiHandler("searchSuggest")
	void searchKeyPress(KeyPressEvent e) {
		if (e.getCharCode() == KeyCodes.KEY_ENTER) {
			currentEntity = null;
			currentSearchQuery = suggestText.getText();
			currentPage = 0;
			refreshContent();
		}
	}

	@UiHandler("searchButton")
	void searchClick(ClickEvent e) {
		currentEntity = null;
		currentSearchQuery = suggestText.getText();
		currentPage = 0;
		refreshContent();
	}

	@UiHandler("addSectionButton")
	void addSectionClick(ClickEvent e) {
		if (ConversationManager.getCurrentUser() != null)
			addSectionPopup.center();
	}

	@UiHandler("firstButton")
	void firstClick(ClickEvent e) {
		currentPage = 0;
		refreshContent();
	}

	@UiHandler("previousButton")
	void previousClick(ClickEvent e) {
		--currentPage;
		refreshContent();
	}

	@UiHandler("nextButton")
	void nextClick(ClickEvent e) {
		++currentPage;
		refreshContent();
	}

	@UiHandler("lastButton")
	void lastClick(ClickEvent e) {
		currentPage = pagesNumber - 1;
		refreshContent();
	}

	@UiHandler("addEntryButton")
	void addEntryClick(ClickEvent e) {
		if (ConversationManager.getCurrentUser() != null)
			addEntryPopup.center();
	}

	private static class EntitySuggestion extends MultiWordSuggestOracle.MultiWordSuggestion {

		private Entity entity;

		private EntitySuggestion() {}

		private EntitySuggestion(Entity entity, String replacementString, String displayString) {
			super(replacementString, displayString);
			this.entity = entity;
		}
	}
}
