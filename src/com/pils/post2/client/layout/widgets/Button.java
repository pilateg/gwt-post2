package com.pils.post2.client.layout.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FocusWidget;

public class Button extends FocusWidget {
	public Button() {
		setElement(Document.get().createDivElement());
		Resources.INSTANCE.css().ensureInjected();
		getElement().addClassName(Resources.INSTANCE.css().button());
	}

	public Button(String html) {
		this();
		getElement().setInnerHTML(html);
	}

	public void setText(String text) {
		getElement().setInnerText(text);
	}

	public void setHtml(String html) {
		getElement().setInnerHTML(html);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled)
			getElement().removeClassName(Resources.INSTANCE.css().disabled());
		else
			getElement().addClassName(Resources.INSTANCE.css().disabled());
	}

	public interface Resources extends ClientBundle {
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("../styles/button.css")
		Css css();

		public interface Css extends CssResource {
			String button();

			String disabled();
		}
	}
}
