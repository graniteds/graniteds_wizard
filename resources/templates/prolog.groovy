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
	
	/**
	 * Returns a URI pointing to the artifact specified by the given coordinate. By
	 * default, the returned URI is relative to "http://repo2.maven.org/maven2/".
	 * 
	 * @param coordinate a Maven coordinate in the form of
	 *		"groupId:artifactId[:packaging[:classifier]]:version".
	 * @return a URI pointing to the artifact specified by the given coordinate.
	 */
	static URI mvn2Uri(String coordinate) {
		return mvn2Uri("http://repo2.maven.org/maven2/", coordinate);
	}
	
	/**
	 * Returns a URI pointing to the artifact specified by the given coordinate. By
	 * default, the returned URI is relative to "http://repo2.maven.org/maven2/".
	 * 
	 * @param repository a Maven repository URL (eg. "http://repo2.maven.org/maven2/").
	 * @param coordinate a Maven coordinate in the form of
	 *		"groupId:artifactId[:packaging[:classifier]]:version".
	 * @return the string representation of the parameter with '.'
	 * 		replaced by slashes (or null if o was null).
	 */
	static URI mvn2Uri(String repository, String coordinate) {
    	String[] tokens = coordinate.split(":", 5);
    	if (tokens.length < 3) {
    		throw new IllegalArgumentException(
    			"Illegal Maven coordinate: " + coordinate +
    			" (must be \"groupId:artifactId[:packaging[:classifier]]:version\")"
    		);
    	}
    	
    	String groupId = tokens[0];
    	String artifactId = tokens[1];
    	String version = tokens[tokens.length - 1];
    	String packaging = (tokens.length > 3 ? tokens[2] : "jar");
    	String classifier = (tokens.length > 4 ? tokens[3] : null);
    	
    	StringBuilder path = new StringBuilder(repository);
    	if (!repository.endsWith("/"))
    		path.append('/');
    	path.append(groupId.replace('.', '/')).append('/');
    	path.append(artifactId).append('/');
    	path.append(version).append('/');
    	
    	path.append(artifactId).append('-').append(version);
    	if (classifier != null)
    		path.append('-').append(classifier);
    	path.append('.').append(packaging);
    	
    	try {
    		return new URI(path.toString());
    	}
    	catch (URISyntaxException e) {
    		throw new RuntimeException("Could not create URI from Maven coordinate: " + coordinate, e);
    	}
    }
}

// Make sure the class is "exported" and can be used in subsequent scripts or expressions.
Fs;

GDS_REV = "2.3.0.GA"
