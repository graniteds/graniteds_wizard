/**
 * Global utility class with methods available in all other Groovy scripts or expressions.
 */
class Fs {

	/**
	 * Escape (double) backslashes characters. This method is useful when
	 * generating standard properties files because single backslash characters
	 * are discarded at load time.
	 * 
	 * @param o an object whose string representation must be escaped.
	 * @return the string representation of the parameter with escaped
	 * 		backslashes (or null if o was null).
	 */
	static String escapeBackslashes(Object o) {
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
	static String dotsToSlashes(Object o) {
		if (o == null)
			return null;
		
		return o.toString().replace('.', '/');
	}
}

// Make sure the class is "exported" and can be used in subsequent scripts or expressions.
Fs;

// GraniteDS version.
GRANITEDS_VERSION = "2.3.2.GA";

// URL prefix for downloading Maven artifacts.
DEFAULT_MAVEN_REPOSITORY = "http://repo2.maven.org/maven2/";
