package com.pils.post2.client;

public class ClientUtils {

	public static void log(Object... args) {
		for (Object o : args)
			log(o);
	}

	private static native boolean isSupported() /*-{
      return ((window.console != null) &&
          (window.console.log != null) &&
          (typeof(window.console.log) == 'function'));
  }-*/;

	private static native void log(Object message) /*-{
      window.console.log(message);
  }-*/;
}
