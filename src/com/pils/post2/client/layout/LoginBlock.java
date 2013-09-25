package com.pils.post2.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.pils.post2.client.ClientUtils;
import com.pils.post2.client.conversation.ConversationService;

class LoginBlock extends UiBlock {
	private SimplePanel mainPanel = GWT.create(SimplePanel.class);

	public LoginBlock() {
		initWidget(mainPanel);
		Button button = new Button("Click me");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ConversationService.App.getInstance().getMessage("Hello, World!", new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						ClientUtils.log(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("!");
					}
				});
			}
		});
		mainPanel.add(button);
	}

	@Override
	public void render() {
	}
}
