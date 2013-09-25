package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.layout.BlockFactory;
import com.pils.post2.client.layout.UiBlockHandler;

public class Post2 implements EntryPoint {
	private UiBlockHandler blockHandler = GWT.create(UiBlockHandler.class);

	public void onModuleLoad() {
		RootLayoutPanel.get().add(blockHandler);

		blockHandler.addUiBlock(BlockFactory.getInstance().getLoginBlock());
	}
}
