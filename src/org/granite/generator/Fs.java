/*
  GRANITE DATA SERVICES
  Copyright (C) 2011 GRANITE DATA SERVICES S.A.S.

  This file is part of Granite Data Services.

  Granite Data Services is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published by
  the Free Software Foundation; either version 2 of the License, or (at your
  option) any later version.

  Granite Data Services is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License
  for more details.

  You should have received a copy of the GNU Library General Public License
  along with this library; if not, see <http://www.gnu.org/licenses/>.
*/

package org.granite.generator;

/**
 * Utility class intended to be used in Groovy scripts.
 * 
 * @author Franck WOLFF
 */
public class Fs {

	/**
	 * Escape (double) backslashes characters. This method is useful when
	 * generating standard properties files because single backslash characters
	 * are discarded at load time.
	 * 
	 * @param o an object whose string representation must be escaped.
	 * @return the string representation of the parameter with escaped
	 * 		backslashes (or null if o was null).
	 */
	public static String escapeBackslashes(Object o) {
		if (o == null)
			return null;
		
		return o.toString().replace("\\", "\\\\");
	}

	/**
	 * Replace all '.' characters file by '/' characters. This method is useful
	 * when specifying package in directory names (eg.
	 * java-source/${Fs.dotsToSlashes(packageName)}/MyClass.java).
	 * 
	 * @param o an object whose string representation must be transformed.
	 * @return the string representation of the parameter with '.'
	 * 		replaced by slashes (or null if o was null).
	 */
	public static String dotsToSlashes(Object o) {
		if (o == null)
			return null;
		
		return o.toString().replace('.', '/');
	}
}
