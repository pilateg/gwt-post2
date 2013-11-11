package com.pils.post2.client.uiblocks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pils.post2.shared.dto.Comment;
import com.pils.post2.shared.dto.Entity;
import com.pils.post2.shared.dto.Entry;
import com.pils.post2.shared.dto.Section;

public class BreadcrumbBlock extends Composite {

	private FlowPanel mainPanel = GWT.create(FlowPanel.class);

	public BreadcrumbBlock() {
		BreadcrumbResources.INSTANCE.css().ensureInjected();
		mainPanel.addStyleName(BreadcrumbResources.INSTANCE.css().block());
		initWidget(mainPanel);
	}
	
	public void setBreadcrumb(Entity entity) {
		clear();
		switch (entity.getType()) {
			case Comment:
				Comment comment = (Comment) entity;
				addBreadcrumb(comment.getEntry().getSection().getOwner());
				addBreadcrumb(comment.getEntry().getSection());
				addBreadcrumb(comment.getEntry());
				break;
			case Entry:
				Entry entry = (Entry) entity;
				addBreadcrumb(entry.getSection().getOwner());
				addBreadcrumb(entry.getSection());
				addBreadcrumb(entry);
				break;
			case Section:
				Section section = (Section) entity;
				addBreadcrumb(section.getOwner());
				addBreadcrumb(section);
				break;
			case Tag:
			case User:
				addBreadcrumb(entity);
				break;
		}
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

	public void clear() {
		mainPanel.clear();
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
