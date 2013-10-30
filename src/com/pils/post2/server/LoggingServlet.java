package com.pils.post2.server;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;

public class LoggingServlet extends RemoteLoggingServiceImpl {
	public LoggingServlet() {
		setSymbolMapsDirectory("WEB-INF/deploy/Post2/symbolMaps");
	}
}
