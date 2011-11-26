package org.granite.generator;

public class ScriptingEngineFactory {

	public static ScriptingEngine createInstance() {
		return new GroovyEngine();
	}	
}
