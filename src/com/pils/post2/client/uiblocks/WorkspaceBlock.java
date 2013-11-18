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

public class WorkspaceBlock extends Composite {
	interface WorkspaceBlockUiBinder extends UiBinder<DockLayoutPanel, WorkspaceBlock> {}
	private static WorkspaceBlockUiBinder uiBinder = GWT.create(WorkspaceBlockUiBinder.class);

	public static final WorkspaceBlock INSTANCE = new WorkspaceBlock();

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
				selectEntity(((EntitySuggestion) event.getSelectedItem()).entity);
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

	private void initContentBlock() {
		final TextBox title = new TextBox();
		addEntryPopup.addWidget(title);
		final TextArea content = new TextArea();
		addEntryPopup.addWidget(content);
		addEntryPopup.setButtons("create entry", "cancel",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent createEvent) {
						if (content.getText() == null || content.getText().isEmpty())
							return;
						final Entry entry = new Entry();
						entry.setTitle(title.getText());
						entry.setContent(content.getText());
						if (currentEntity instanceof Section)
							entry.setSection((Section) currentEntity);
						ConversationManager.addEntry(entry, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									addEntryPopup.hide();
									title.setText("");
									content.setText("");
									//update content panel
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
						title.setText("");
						content.setText("");
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

		Entity entity;

		private EntitySuggestion() {}

		private EntitySuggestion(String replacementString, String displayString) {
			super(replacementString, displayString);
		}
	}
}
