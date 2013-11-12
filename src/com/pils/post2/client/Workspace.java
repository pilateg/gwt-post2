package com.pils.post2.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.client.uiblocks.EntityLinkBlock;
import com.pils.post2.client.uiblocks.EntryBlock;
import com.pils.post2.client.uiblocks.EntryDetailedBlock;
import com.pils.post2.client.uiblocks.PopupBlock;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workspace extends Composite {
	interface WorkspaceUiBinder extends UiBinder<DockLayoutPanel, Workspace> {}
	private static WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);

	public static final Workspace INSTANCE = new Workspace();

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
	@UiField protected Button firstButton;
	@UiField protected Button previousButton;
	@UiField protected Button nextButton;
	@UiField protected Button lastButton;
	@UiField protected FlowPanel navigationPanel;

	private int itemsNumber;
	private int itemsOnPage;
	private int pagesNumber;
	private int currentPage = -1;
	private Section currentSection;

	private Workspace() {
		initAuthenticationCallbacks();
		initSearchBlock();
		initSectionsBlock();
		initContentBlock();

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
					breadcrumbPanel.clear();
				}
			}
		});
	}

	private void initSearchBlock() {
		searchSuggest = new SuggestBox(new SuggestOracle() {
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				ConversationManager.lightSearch(suggestText.getText(), new ConversationCallback<List<? extends Entity>>() {
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
						entry.setSection(currentSection);
						ConversationManager.addEntry(entry, new ConversationCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									addEntryPopup.hide();
									title.setText("");
									content.setText("");
									//add entry to db and update
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
		setBreadcrumb(e);
		switch (e.getType()) {
			case Comment:
				Comment comment = (Comment) e;
				setEntry(comment);
				navigationPanel.setVisible(false);
				break;
			case Entry:
				Entry entry = (Entry) e;
				setEntry(entry);
				navigationPanel.setVisible(false);
				break;
			case Section:
				Section section = (Section) e;
				setEntries(section.getEntries());
				navigationPanel.setVisible(true);
				setUp(section.getEntries().size(), ConversationManager.getItemsOnPage());
				setCurrentPage(0);
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

	private void setUp(int itemsNumber, int itemsOnPage) {
		if (itemsNumber != this.itemsNumber || itemsOnPage != this.itemsOnPage) {
			pagesNumber = itemsNumber / itemsOnPage + (itemsNumber % itemsOnPage == 0 ? 0 : 1);
			this.itemsNumber = itemsNumber;
			this.itemsOnPage = itemsOnPage;
			navigationPanel.clear();
			for (int i = 0; i < pagesNumber; ++i) {
				final Button button = new Button(String.valueOf(i + 1));
				button.getElement().getStyle().setProperty("display", "table-cell");
				final int finalI = i;
				button.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						setCurrentPage(finalI);
					}
				});
				navigationPanel.add(button);
			}
		}
	}

	private void setCurrentPage(int page) {
		if (currentPage != page) {
			if (currentPage != -1)
				((Button) navigationPanel.getWidget(currentPage)).setEnabled(true);
			((Button) navigationPanel.getWidget(page)).setEnabled(false);
			currentPage = page;
			firstButton.setEnabled(currentPage != 0);
			previousButton.setEnabled(currentPage != 0);
			nextButton.setEnabled(currentPage != pagesNumber - 1);
			lastButton.setEnabled(currentPage != pagesNumber - 1);
			onPageSelected(currentPage, itemsOnPage);
		}
	}

	private void onPageSelected(int pageNumber, int itemsOnPage) {
		int itemsNumber = 20;
		List<Entity> entries = new ArrayList<Entity>(itemsNumber);
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

		if (pageNumber == -1) {
			contentPanel.clear();
			return;
		}
		int from = pageNumber * itemsOnPage;
		int to = from + itemsOnPage > itemsNumber ? itemsNumber : from + itemsOnPage;
		setEntries(entries.subList(from, to));
	}

	private void setBreadcrumb(Entity entity) {
		breadcrumbPanel.clear();
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
	}

	private void setEntries(List<? extends Entity> entities) {
		resetContentPanel(entities == null || entities.isEmpty() ? null : entities.get(0));
		if (entities != null)
			for (Entity entity : entities)
				contentPanel.add(new EntryBlock(entity));
	}

	private void setEntry(Entity entity) {
		resetContentPanel(entity);
		if (entity != null)
			contentPanel.add(new EntryDetailedBlock(entity));
	}

	private void resetContentPanel(Entity entity) {
		contentPanel.clear();
		switch (entity.getType()) {
			case Entry:
				currentSection = ((Entry) entity).getSection();
				break;
			case Section:
				currentSection = (Section) entity;
				break;
			default:
				currentSection = null;
		}
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
		if (e.getCharCode() == KeyCodes.KEY_ENTER)
			ConversationManager.search(suggestText.getText(), new ConversationCallback<List<? extends Entity>>() {
				@Override
				public void onSuccess(List<? extends Entity> result) {
					breadcrumbPanel.clear();
					setEntries(result);
					navigationPanel.setVisible(true);
					//setUp(result.size(), ConversationManager.getItemsOnPage());
					setCurrentPage(0);
				}
			});
	}

	@UiHandler("searchButton")
	void searchClick(ClickEvent e) {
		ConversationManager.search(suggestText.getText(), new ConversationCallback<List<? extends Entity>>() {
			@Override
			public void onSuccess(List<? extends Entity> result) {
				breadcrumbPanel.clear();
				setEntries(result);
				navigationPanel.setVisible(true);
				//setUp(result.size(), ConversationManager.getItemsOnPage());
				setCurrentPage(0);
			}
		});
	}

	@UiHandler("addSectionButton")
	void addSectionClick(ClickEvent e) {
		if (ConversationManager.getCurrentUser() != null)
			addSectionPopup.center();
	}

	@UiHandler("firstButton")
	void firstClick(ClickEvent e) {
		setCurrentPage(0);
	}

	@UiHandler("previousButton")
	void previousClick(ClickEvent e) {
		setCurrentPage(currentPage - 1);
	}

	@UiHandler("nextButton")
	void nextClick(ClickEvent e) {
		setCurrentPage(currentPage + 1);
	}

	@UiHandler("lastButton")
	void lastClick(ClickEvent e) {
		setCurrentPage(pagesNumber - 1);
	}

	@UiHandler("addEntryButton")
	void addEntryClick(ClickEvent e) {
		if (ConversationManager.getCurrentUser() != null)
			addEntryPopup.center();
	}

	private class EntitySuggestion extends MultiWordSuggestOracle.MultiWordSuggestion {

		Entity entity;

		private EntitySuggestion() {}

		private EntitySuggestion(String replacementString, String displayString) {
			super(replacementString, displayString);
		}
	}
}
