package com.pils.post2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pils.post2.client.layout.Resources;
import com.pils.post2.client.uiblocks.WorkspaceBlock;
import com.pils.post2.shared.conversation.ConversationManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Post2 implements EntryPoint {

	private Logger logger = Logger.getLogger("");

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				logger.log(Level.SEVERE, "!!:", unwrap(e));
			}
		});
		Resources.INSTANCE.css().ensureInjected();
		init();
		ConversationManager.restoreSession();
	}

	public static void init() {
		RootLayoutPanel.get().add(WorkspaceBlock.INSTANCE);
	}

	public static Throwable unwrap(Throwable e) {
		if (e instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) e;
			if (ue.getCauses().size() == 1) {
				return unwrap(ue.getCauses().iterator().next());
			}
		}
		return e;
	}
}
