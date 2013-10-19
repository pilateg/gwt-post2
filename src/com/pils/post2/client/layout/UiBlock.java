package com.pils.post2.client.layout;

import com.google.gwt.user.client.ui.Composite;

public abstract class UiBlock extends Composite {
	protected UiBlock() {
		Resources.INSTANCE.css().ensureInjected();
	}
}
