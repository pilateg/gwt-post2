package com.pils.post2.client.layout;

import com.google.gwt.core.client.GWT;

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
