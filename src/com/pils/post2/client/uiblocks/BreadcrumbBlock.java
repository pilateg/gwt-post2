package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.shared.dto.Entity;

public class BreadcrumbBlock extends Composite {
	private FlowPanel mainPanel = GWT.create(FlowPanel.class);

	public BreadcrumbBlock() {
		BreadcrumbResources.INSTANCE.css().ensureInjected();
		mainPanel.addStyleName(BreadcrumbResources.INSTANCE.css().block());
		initWidget(mainPanel);
	}

	public void addBreadcrumb(Entity entity) {
		if (entity != null)
			mainPanel.add(new EntityLinkBlock(entity));
	}

	public EntityLinkBlock removeBreadcrumb() {
		if (mainPanel.getWidgetCount() > 0) {
			EntityLinkBlock block = (EntityLinkBlock) mainPanel.getWidget(mainPanel.getWidgetCount() - 1);
			mainPanel.remove(block);
			return block;
		}
		return null;
	}

	public interface BreadcrumbResources extends ClientBundle {
		BreadcrumbResources INSTANCE = GWT.create(BreadcrumbResources.class);

		@Source("../layout/styles/breadcrumb.css")
		Css css();

		interface Css extends CssResource {
			String block();
		}
	}
}
