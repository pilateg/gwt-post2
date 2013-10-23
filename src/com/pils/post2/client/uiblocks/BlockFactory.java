package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.pils.post2.client.layout.UiBlock;

public class BlockFactory {
	private static BlockFactory instance = GWT.create(BlockFactory.class);

	private UiBlock loginBlock = GWT.create(LoginBlock.class);
	private UiBlock searchBlock = GWT.create(SearchBlock.class);
	private UiBlock categoriesBlock = GWT.create(CategoriesBlock.class);
	private UiBlock contentBlock = GWT.create(ContentBlock.class);

	private BlockFactory() {
	}

	public static BlockFactory getInstance() {
		return instance;
	}

	public UiBlock getLoginBlock() {
		return loginBlock;
	}

	public UiBlock getSearchBlock() {
		return searchBlock;
	}

	public UiBlock getCategoriesBlock() {
		return categoriesBlock;
	}

	public UiBlock getContentBlock() {
		return contentBlock;
	}

	public UiBlock getEntryBlock() {
		return GWT.create(EntryBlock.class);
	}

	public UiBlock getBreadcrumbBlock() {
		return GWT.create(BreadcrumbBlock.class);
	}

	public UiBlock getNavigationBlock() {
		return GWT.create(NavigationBlock.class);
	}

	public UiBlock getEntityLinkBlock() {
		return GWT.create(EntityLinkBlock.class);
	}
}
