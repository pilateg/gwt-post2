package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.shared.conversation.ConversationCallback;
import com.pils.post2.shared.conversation.ConversationManager;
import com.pils.post2.shared.dto.User;

public class LoginBlock extends Composite {
	interface LoginBlockUiBinder extends UiBinder<FlowPanel, LoginBlock> {
	}

	private static LoginBlockUiBinder uiBinder = GWT.create(LoginBlockUiBinder.class);

	@UiField
	FlowPanel mainPanel;
	@UiField
	FlowPanel loginPanel;
	@UiField
	TextBox name;
	@UiField
	TextBox pass;
	@UiField
	Button loginButton;
	@UiField
	Button registerButton;
	@UiField
	FlowPanel loggedPanel;
	@UiField
	Label userName;
	@UiField
	Button logoutButton;

	public LoginBlock() {
		initWidget(uiBinder.createAndBindUi(this));
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		name.getElement().setAttribute("placeholder", "name");
		pass.getElement().setAttribute("placeholder", "pass");
		KeyPressHandler keyPressHandler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER)
					login();
			}
		};
		name.addKeyPressHandler(keyPressHandler);
		pass.addKeyPressHandler(keyPressHandler);
		loginButton.setText("Log in");
		registerButton.setText("Register");
	}

	public void setUser(User user) {
		if (user != null) {
			userName.setText(user.getName());
			loginPanel.setVisible(false);
			loggedPanel.setVisible(true);
		} else {
			name.setText("");
			pass.setText("");
			loginPanel.setVisible(true);
			loggedPanel.setVisible(false);
		}
	}

	private void login() {
		ConversationManager.login(name.getText(), pass.getText());
	}

	@UiHandler("loginButton")
	void loginClick(ClickEvent e) {
		login();
	}

	@UiHandler("logoutButton")
	void logoutClick(ClickEvent e) {
		ConversationManager.logout();
	}
}
