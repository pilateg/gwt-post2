package com.pils.post2.client.uiblocks;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class SearchBlock extends UiBlock {
	public SearchBlock() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		SuggestBox text = new SuggestBox();
		text.getElement().setAttribute("placeholder", "search");
		mainPanel.add(text);
		initWidget(mainPanel);
	}
}
