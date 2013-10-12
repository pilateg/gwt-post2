package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.pils.post2.client.layout.UiBlock;

public class BlockFactory {
	private static BlockFactory instance = GWT.create(BlockFactory.class);

	private UiBlock loginBlock = GWT.create(LoginBlock.class);

	private BlockFactory() {
	}

	public static BlockFactory getInstance() {
		return instance;
	}

	public UiBlock getLoginBlock() {
		return loginBlock;
	}
}
