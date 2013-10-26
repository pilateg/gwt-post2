package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
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
		NavigationResources.INSTANCE.css().ensureInjected();
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		mainPanel.addStyleName(NavigationResources.INSTANCE.css().mainPanel());
		first = new Button("<<");
		first.addStyleName(NavigationResources.INSTANCE.css().leftButton());
		mainPanel.add(first);
		previous = new Button("<");
		previous.addStyleName(NavigationResources.INSTANCE.css().leftButton());
		mainPanel.add(previous);
		panel = new FlowPanel();
		ScrollPanel scrollPanel = new ScrollPanel(panel);
		scrollPanel.addStyleName(NavigationResources.INSTANCE.css().scroll());
		mainPanel.add(scrollPanel);
		next = new Button(">");
		next.addStyleName(NavigationResources.INSTANCE.css().rightButton());
		mainPanel.add(next);
		last = new Button(">>");
		last.addStyleName(NavigationResources.INSTANCE.css().rightButton());
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
			if (currentPage != -1)
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

	public interface NavigationResources extends ClientBundle {
		NavigationResources INSTANCE = GWT.create(NavigationResources.class);

		@Source("../layout/styles/navigation.css")
		Css css();

		public interface Css extends CssResource {
			String scroll();

			String leftButton();

			String rightButton();

			String mainPanel();
		}
	}
}
