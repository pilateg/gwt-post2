package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;

public class SearchBlock extends UiBlock {
	public SearchBlock() {
		FlowPanel mainPanel = GWT.create(FlowPanel.class);
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		SuggestBox text = GWT.create(SuggestBox.class);
		text.getElement().setAttribute("placeholder", "search");
		mainPanel.add(text);
		initWidget(mainPanel);
	}
}
