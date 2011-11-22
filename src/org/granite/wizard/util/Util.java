package org.granite.wizard.util;

public class Util {

	public static String escapeBackslashes(Object o) {
		if (o == null)
			return null;
		return o.toString().replace("\\", "\\\\");
	}
}
