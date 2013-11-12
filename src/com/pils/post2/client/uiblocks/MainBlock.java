package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.Comment;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.Section;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainBlock extends Composite {
	interface MainBlockUiBinder extends UiBinder<FlowPanel, MainBlock> {}
	private static MainBlockUiBinder uiBinder = GWT.create(MainBlockUiBinder.class);

	public static final MainBlock INSTANCE = new MainBlock();

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

	private MainBlock() {
		initContentBlock();
		initWidget(uiBinder.createAndBindUi(this));
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

	protected void setUp(int itemsNumber, int itemsOnPage) {
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

	protected void setCurrentPage(int page) {
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

	protected void setBreadcrumb(Entity entity) {
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

	void setEntries(List<? extends Entity> entities) {
		resetContentPanel(entities == null || entities.isEmpty() ? null : entities.get(0));
		if (entities != null)
			for (Entity entity : entities)
				contentPanel.add(new EntryBlock(entity));
	}

	protected void setEntry(Entity entity) {
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
}
