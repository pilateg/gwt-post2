package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.pils.post2.client.conversation.ConversationCallback;
import com.pils.post2.client.conversation.ConversationManager;
import com.pils.post2.client.conversation.dto.User;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.UiBlock;
import com.pils.post2.client.layout.widgets.Button;

public class LoginBlock extends UiBlock {
	interface LoginBlockUiBinder extends UiBinder<FlowPanel, LoginBlock> {}

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
		name.setText("name");
		pass.setText("pass");
		loginButton.setText("Log in");
		registerButton.setText("Register");
	}

	@UiHandler("loginButton")
	void loginClick(ClickEvent e) {
		ConversationManager.login(name.getText(), pass.getText(), new ConversationCallback<User>() {
			@Override
			public void onSuccess(User user) {
				if (user != null) {
					userName.setText(user.getName());
					loginPanel.setVisible(false);
					loggedPanel.setVisible(true);
				}
			}
		});
	}

	@UiHandler("logoutButton")
	void logoutClick(ClickEvent e) {
		ConversationManager.logout(new ConversationCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					name.setText("");
					pass.setText("");
					loginPanel.setVisible(true);
					loggedPanel.setVisible(false);
				}
			}
		});
	}
}
