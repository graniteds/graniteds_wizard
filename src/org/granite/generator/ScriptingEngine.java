package org.granite.generator;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ScriptingEngine {
	
	void load(File scriptFile, Map<String, Object> variables) throws IOException;
	
	boolean execute(File template, File target, Map<String, Object> variables) throws IOException;
	
	<K, V> Map<K, V> evaluate(File scriptFile, Map<String, Object> variables) throws IOException;
	
	String evaluate(String expression, Map<String, Object> variables);
}
