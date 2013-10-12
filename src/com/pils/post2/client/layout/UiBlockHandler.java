package com.pils.post2.client.layout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class UiBlockHandler extends DockLayoutPanel {

	public UiBlockHandler() {
		this(Style.Unit.PX);
	}

	public UiBlockHandler(Style.Unit unit) {
		super(unit);
	}
}
