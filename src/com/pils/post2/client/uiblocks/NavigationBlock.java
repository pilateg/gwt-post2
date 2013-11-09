package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.pils.post2.client.layout.widgets.Button;

public class NavigationBlock extends Composite {
	private int itemsNumber;
	private int itemsOnPage;
	private int pagesNumber;
	private int currentPage = -1;

	private PageSelectionHandler clickHandler;
	private HorizontalPanel mainPanel;
	private ScrollPanel scrollPanel;
	private FlowPanel panel;
	private Button first;
	private Button previous;
	private Button next;
	private Button last;

	public NavigationBlock() {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
		NavigationResources.INSTANCE.css().ensureInjected();
		mainPanel.addStyleName(NavigationResources.INSTANCE.css().mainPanel());
		first = addButton("<<", NavigationResources.INSTANCE.css().leftButton(), "30px");
		previous = addButton("<", NavigationResources.INSTANCE.css().leftButton(), "30px");
		panel = new FlowPanel();
		scrollPanel = new ScrollPanel(panel);
		mainPanel.add(scrollPanel);
		next = addButton(">", NavigationResources.INSTANCE.css().rightButton(), "30px");
		last = addButton(">>", NavigationResources.INSTANCE.css().rightButton(), "30px");
	}

	private Button addButton(String text, String className, String width) {
		Button button = new Button(text);
		button.addStyleName(className);
		mainPanel.add(button);
		mainPanel.setCellWidth(button, width);
		return button;
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
			button.getElement().getStyle().setProperty("display", "table-cell");
			final int finalI = i;
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					setCurrentPage(finalI);
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
			}
		});
		previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(currentPage - 1);
			}
		});
		next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(currentPage + 1);
			}
		});
		last.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCurrentPage(pagesNumber - 1);
			}
		});
	}

	public void setCurrentPage(int page) {
		if (currentPage != page) {
			if (currentPage != -1)
				setButtonEnabled((Button) panel.getWidget(currentPage), true);
			setButtonEnabled((Button) panel.getWidget(page), false);
			currentPage = page;
			scrollPanel.ensureVisible(panel.getWidget(currentPage));
			setButtonEnabled(first, currentPage != 0);
			setButtonEnabled(previous, currentPage != 0);
			setButtonEnabled(next, currentPage != pagesNumber - 1);
			setButtonEnabled(last, currentPage != pagesNumber - 1);
			clickHandler.onPageSelected(currentPage, itemsOnPage);
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

	public interface NavigationResources extends ClientBundle {
		NavigationResources INSTANCE = GWT.create(NavigationResources.class);

		@Source("../layout/styles/navigation.css")
		Css css();

		interface Css extends CssResource {
			String leftButton();

			String rightButton();

			String mainPanel();
		}
	}
}
