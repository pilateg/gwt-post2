package com.pils.post2.client;

public class ClientUtils {

	private ClientUtils() {
	}

	public static String escape(String s) {
		return s.replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;").replace("\"","&quot;");
	}

	public static String trim(String str, int length) {
		return str.length() < length ? str : str.substring(0, length - 1) + "…";
	}

	public static void log(Object... args) {
		if (isSupported())
			for (Object o : args)
				jsLog(o);
	}

	private static native boolean isSupported() /*-{
      return ((window.console != null) &&
          (window.console.log != null) &&
          (typeof(window.console.log) == 'function'));
  }-*/;

	private static native void jsLog(Object message) /*-{
      window.console.log(message);
  }-*/;
}
