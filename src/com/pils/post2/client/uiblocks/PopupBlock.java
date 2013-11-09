package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.widgets.Button;

public class PopupBlock extends Composite {

	private PopupPanel popup;
	private FlowPanel panel;
	private Button addButton;
	private Button cancelButton;

	public PopupBlock() {
		popup = new PopupPanel(false, true);
		PopupResources.INSTANCE.css().ensureInjected();
		popup.addStyleName(Resources.INSTANCE.css().block());
		popup.addStyleName(PopupResources.INSTANCE.css().popup());
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
		popup.setWidget(verticalPanel);
		panel = new FlowPanel();
		FlowPanel buttonPanel = new FlowPanel();
		verticalPanel.add(panel);
		verticalPanel.add(buttonPanel);
		addButton = new Button();
		cancelButton = new Button();
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
	}

	public void addWidget(Widget widget) {
		panel.add(widget);
	}

	public void setButtons(String addTitle, String cancelTitle, ClickHandler addHandler, ClickHandler cancelHandler) {
		addButton.setText(addTitle);
		cancelButton.setText(cancelTitle);
		addButton.addClickHandler(addHandler);
		cancelButton.addClickHandler(cancelHandler);
	}

	public void center() {
		popup.center();
	}

	public void hide() {
		popup.hide();
	}

	public interface PopupResources extends ClientBundle {
		PopupResources INSTANCE = GWT.create(PopupResources.class);

		@Source("../layout/styles/popup.css")
		Css css();

		interface Css extends CssResource {
			String popup();
		}
	}
}