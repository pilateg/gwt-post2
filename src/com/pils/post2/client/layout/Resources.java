package com.pils.post2.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Resources extends ClientBundle {
	Resources INSTANCE = GWT.create(Resources.class);

	@Source("styles/styles.css")
	Css css();

	interface Css extends CssResource {
		String block();

		String leftButton();

		String navigationPanel();

		String rightButton();
	}
}
