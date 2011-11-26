package org.granite.generator;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.granite.generator.gsp.GroovyRenderer;
import org.granite.generator.gsp.ParseException;
import org.granite.generator.gsp.Parser;
import org.granite.generator.gsp.token.Token;
import org.granite.generator.util.SourceUtil;
import org.granite.generator.util.URIUtil;

public class GroovyEngine implements ScriptingEngine {

	private final GroovyShell shell;
	
	private final Map<String, Object> globalVariables;
	
	public GroovyEngine() {
		shell = new GroovyShell(getClass().getClassLoader());
		globalVariables = new HashMap<String, Object>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void load(File scriptFile, Map<String, Object> variables) throws IOException {
        Script script = shell.parse(scriptFile);
        if (variables != null)
        	script.setBinding(new Binding(new HashMap<String, Object>(variables)));
        else
        	script.setBinding(new Binding());
        script.run();
        
        Map<String, Object> result = script.getBinding().getVariables();
        checkVariableNames(result);
        globalVariables.putAll(result);
	}

	@Override
	public synchronized boolean execute(File template, File target, Map<String, Object> variables) throws IOException {
		if (variables != null)
			checkVariableNames(variables);

        URI uri = template.toURI();
        InputStream is = null;
        String source = null;

        // Write in memory (step 1).
        PublicByteArrayOutputStream buffer = new PublicByteArrayOutputStream(8192);
        try {
            is = URIUtil.getInputStream(uri, getClass().getClassLoader());
            Reader reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));

            Parser parser = new Parser();
            List<Token> tokens = parser.parse(reader);

            GroovyRenderer renderer = new GroovyRenderer();
            source = renderer.renderSource(tokens);
            Script script = shell.parse(source);
            
            Map<String, Object> context = new HashMap<String, Object>(this.globalVariables);
    		if (variables != null)
    			context.putAll(variables);
            script.setBinding(new Binding(context));
    		
            PrintWriter out = new PrintWriter(new OutputStreamWriter(buffer));
            script.setProperty("out", out);
            script.run();
            out.flush();
        }
        catch (ParseException e) {
            throw new RuntimeException("Could not parse template: " + uri, e);
        }
        catch (CompilationFailedException e) {
        	String newLine = System.getProperty("line.separator");
            throw new RuntimeException("Could not compile template: " + uri, new RuntimeException(newLine + SourceUtil.numberize(source, newLine), e));
        }
        catch (MissingPropertyException e) {
            throw new RuntimeException("Could not execute template: " + uri, e);
        }
        catch (CancelFileGenerationException e) {
        	return false;
        }
        finally {
        	if (is != null)
                is.close();
        }
        
        // Write in target file (step 2).
		if (target.getParentFile() != null && !target.getParentFile().exists())
			target.getParentFile().mkdirs();
		
        OutputStream os = null;
		try {
			os = new FileOutputStream(target);
			os.write(buffer.getBytes(), 0, buffer.size());
		} finally {
			if (os != null)
				os.close();
		}
        
        return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <K, V> Map<K, V> evaluate(File scriptFile, Map<String, Object> variables) throws IOException {
        Script script = shell.parse(scriptFile);
        
        DualContextMap<String, Object> context = new DualContextMap<String, Object>();
        context.putAllGlobal(globalVariables);
        if (variables != null)
        	context.putAllGlobal(variables);
        script.setBinding(new Binding(context));
        script.run();
        
        for (String globalKey : globalVariables.keySet())
        	context.remove(globalKey);
        
        return (Map<K, V>)context;
	}

	@Override
	public synchronized String evaluate(String expression, Map<String, Object> variables) {
        Map<String, Object> context = new HashMap<String, Object>(globalVariables);
        if (variables != null)
        	context.putAll(variables);
		
        boolean resolved = true;
		int iStart = -1, iEnd = -1;
		while ((iStart = expression.indexOf("${")) != -1 && resolved) {
			resolved = false;
			iEnd = expression.indexOf('}', iStart + 2);
			if (iEnd != -1) {
				String expr = expression.substring(iStart + 2, iEnd);
				Script script = shell.parse(expr);
		        script.setBinding(new Binding(context));
				Object value = script.run();
				if (value != null) {
					expression = expression.substring(0, iStart) + value + expression.substring(iEnd + 1);
					resolved = true;
				}
			}
		}
		return expression;
	}
	
	@Override
	public synchronized Object getGlobalVariable(String name) {
		return globalVariables.get(name);
	}

	protected void checkVariableNames(Map<String, Object> variables) {
        if (variables.containsKey("out"))
        	throw new RuntimeException("'out' is a reserved variable name for template execution");
	}
	
	static class PublicByteArrayOutputStream extends ByteArrayOutputStream {
		public PublicByteArrayOutputStream() {
		}
		public PublicByteArrayOutputStream(int size) {
			super(size);
		}
		public byte[] getBytes() {
			return buf; // no copy...
		}
	}
	
	static class DualContextMap<K, V> extends LinkedHashMap<K, V> {

		private static final long serialVersionUID = 1L;
		
		private final Map<K, V> global = new HashMap<K, V>();
		
		public DualContextMap() {
		}
		
		public void putAllGlobal(Map<? extends K, ? extends V> map) {
			global.putAll(map);
		}

		@Override
		public V get(Object key) {
			if (super.containsKey(key))
				return super.get(key);
			return global.get(key);
		}
	}
}
