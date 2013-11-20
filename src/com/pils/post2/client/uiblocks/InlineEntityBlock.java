package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.layout.widgets.Button;
import com.pils.post2.shared.dto.Entity;

public class InlineEntityBlock extends Composite {

	private Button cancelButton;

	public InlineEntityBlock(Entity e) {
		InlineEntityResources.INSTANCE.css().ensureInjected();
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName(Resources.INSTANCE.css().block());
		mainPanel.addStyleName(InlineEntityResources.INSTANCE.css().mainPanel());
		mainPanel.getElement().setInnerText(e.getTitle());
		cancelButton = new Button("×");
		mainPanel.add(cancelButton);
		initWidget(mainPanel);
	}

	public void addCancelClickHandler(ClickHandler handler) {
		cancelButton.addClickHandler(handler);
	}

	public interface InlineEntityResources extends ClientBundle {

		InlineEntityResources INSTANCE = GWT.create(InlineEntityResources.class);

		@Source("../layout/styles/inlineEntity.css")
		Css css();

		interface Css extends CssResource {
			String mainPanel();
		}
	}
}
