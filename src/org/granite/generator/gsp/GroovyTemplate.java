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

package org.granite.generator.gsp;

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
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.granite.generator.gsp.token.Token;
import org.granite.generator.util.SourceUtil;
import org.granite.generator.util.URIUtil;
import org.granite.wizard.CancelFileGenerationException;

/**
 * @author Franck WOLFF
 */
public class GroovyTemplate {

	public static boolean executeTemplate(File sourceFile, File targetFile, Map<String, Object> variables) throws IOException {
		ClassLoader classLoader = GroovyTemplate.class.getClassLoader();
		
		URI uri = sourceFile.toURI();
        InputStream is = null;
        String source = null;

        // Write in memory (step 1).
        PublicByteArrayOutputStream buffer = new PublicByteArrayOutputStream(8192);
        try {
            is = URIUtil.getInputStream(uri, classLoader);
            Reader reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));

            Parser parser = new Parser();
            List<Token> tokens = parser.parse(reader);

            GroovyRenderer renderer = new GroovyRenderer();
            source = renderer.renderSource(tokens);
            GroovyShell shell = new GroovyShell(classLoader);
            Script script = shell.parse(source);
            
            Binding binding = new Binding(new HashMap<String, Object>(variables));
            Script scriptInstance = InvokerHelper.createScript(script.getClass(), binding);
    		
            PrintWriter out = new PrintWriter(new OutputStreamWriter(buffer));
            scriptInstance.setProperty("out", out);
            scriptInstance.run();
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
		if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists())
			targetFile.getParentFile().mkdirs();
		
        OutputStream os = null;
		try {
			os = new FileOutputStream(targetFile);
			os.write(buffer.getBytes(), 0, buffer.size());
		} finally {
			if (os != null)
				os.close();
		}
        
        return true;
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
}
