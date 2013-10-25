package com.pils.post2.client.uiblocks;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class NavigationBlock extends UiBlock {
	private int itemsNumber;
	private int itemsOnPage;
	private int pagesNumber;
	private int currentPage = -1;

	private PageSelectionHandler clickHandler;
	private FlowPanel panel;
	private Button first;
	private Button previous;
	private Button next;
	private Button last;

	public NavigationBlock() {
		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		first = new Button("<<");
		mainPanel.add(first);
		previous = new Button("<");
		mainPanel.add(previous);
		panel = new FlowPanel();
		ScrollPanel scrollPanel = new ScrollPanel(panel);
		mainPanel.add(scrollPanel);
		next = new Button(">");
		mainPanel.add(next);
		last = new Button(">>");
		mainPanel.add(last);
	}

	public void setUp(int itemsNumber, int itemsOnPage) {
		if (itemsNumber != this.itemsNumber || itemsOnPage != this.itemsOnPage) {
			pagesNumber = itemsNumber / itemsOnPage + (itemsNumber % itemsOnPage == 0 ? 0 : 1);
			this.itemsNumber = itemsNumber;
			this.itemsOnPage = itemsOnPage;
			update();
		}
	}

	private void update() {
		panel.clear();
		for (int i = 0; i < pagesNumber; ++i) {
			final Button button = new Button(String.valueOf(i + 1));
			final int finalI = i;
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					setCurrentPage(finalI);
					clickHandler.onPageSelected(currentPage, itemsOnPage);
				}
			});
			panel.add(button);
		}
	}

	public void setPageSelectionHandler(final PageSelectionHandler handler) {
		clickHandler = handler;
		first.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(0);
				handler.onPageSelected(currentPage, itemsOnPage);
			}
		});
		previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(currentPage - 1);
				handler.onPageSelected(currentPage, itemsOnPage);
			}
		});
		next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(currentPage + 1);
				handler.onPageSelected(currentPage, itemsOnPage);
			}
		});
		last.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(pagesNumber - 1);
				handler.onPageSelected(currentPage, itemsOnPage);
			}
		});
	}

	public void setCurrentPage(int page) {
		if (currentPage != page) {
			setButtonEnabled((Button) panel.getWidget(currentPage), true);
			setButtonEnabled((Button) panel.getWidget(page), false);
			currentPage = page;
			setButtonEnabled(first, currentPage != 0);
			setButtonEnabled(previous, currentPage != 0);
			setButtonEnabled(next, currentPage != pagesNumber - 1);
			setButtonEnabled(last, currentPage != pagesNumber - 1);
		}
	}

	private static void setButtonEnabled(Button button, boolean enabled) {
		button.setEnabled(enabled);
	}

	public int getItemsNumber() {
		return itemsNumber;
	}

	public int getItemsOnPage() {
		return itemsOnPage;
	}

	public interface PageSelectionHandler {
		void onPageSelected(int pageNumber, int itemsOnPage);
	}
}
