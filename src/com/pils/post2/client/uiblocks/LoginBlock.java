package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.pils.post2.client.conversation.ConversationManager;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;
import com.pils.post2.client.layout.widgets.Button;

public class LoginBlock extends UiBlock {
	interface LoginBlockUiBinder extends UiBinder<FlowPanel, LoginBlock> {}

	private static LoginBlockUiBinder uiBinder = GWT.create(LoginBlockUiBinder.class);

	@UiField
	FlowPanel mainPanel;
	@UiField
	TextBox name;
	@UiField
	TextBox pass;
	@UiField
	Button loginButton;
	@UiField
	Button registerButton;

	public LoginBlock() {
		initWidget(uiBinder.createAndBindUi(this));
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		name.getElement().setAttribute("placeholder", "name");
		pass.getElement().setAttribute("placeholder", "pass");
		loginButton.setText("Log in");
		registerButton.setText("Register");
	}

	@UiHandler("loginButton")
	void handleClick(ClickEvent e) {
		ConversationManager.login(name.getText(), pass.getText());
	}
}
