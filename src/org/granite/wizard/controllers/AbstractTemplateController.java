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

package org.granite.wizard.controllers;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.granite.generator.gsp.GroovyTemplate;
import org.granite.wizard.CancelFileGenerationException;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;
import org.granite.wizard.bindings.Bindings;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.repository.Repository;

/**
 * @author Franck WOLFF
 */
public abstract class AbstractTemplateController {

	protected DynamicProjectWizard wizard;
	protected ProjectTemplate template;
	protected IWizardPage nextPage;

	public DynamicProjectWizard getWizard() {
		return wizard;
	}

	public IWizardPage getStartingPage() {
		return wizard.getStartingPage();
	}
	
	public ProjectTemplate getTemplate() {
		return template;
	}

	public IWizardPage getNextPage() {
		return nextPage;
	}
	public void setNextPage(IWizardPage nextPage) {
		this.nextPage = nextPage;
	}

	public void initialize(DynamicProjectWizard wizard, ProjectTemplate template) {
		this.wizard = wizard;
		this.template = template;
	}
	
	public void createProjectResources(IProject project, File projectTemplateDirectory, SubMonitor monitor, Map<String, Object> variables) throws IOException {
		File projectDir = new File(project.getLocationURI());
		createProjectResources(projectDir, projectTemplateDirectory.toURI(), projectTemplateDirectory, monitor, variables);
	}
	
	protected void createProjectResources(File projectDir, URI projectTemplateURI, File directory, SubMonitor monitor, Map<String, Object> variables) throws IOException {
		
		for (File file : directory.listFiles()) {
			
			if (template.ignoreFile(file))
				continue;
			
			boolean deleteAfterCopy = false;
			
			String relativePath = projectTemplateURI.relativize(file.toURI()).getPath();
			if (relativePath.contains("${"))
				relativePath = resolveVariables(relativePath, variables);
			
			if (file.isDirectory()) {				
				File projectFile = new File(projectDir, relativePath);
				monitor.setTaskName("Creating directory: " + relativePath);
				monitor.setWorkRemaining(80);
				projectFile.mkdirs();
				monitor.worked(1);
				createProjectResources(projectDir, projectTemplateURI, file, monitor, variables);
			}
			else {
				if (file.getName().startsWith("@uri:")) {
					URI uri = resolveUri(file, variables);
					
					if (uri == null) // CancelFileGenerationException.
						continue;

					relativePath = projectTemplateURI.relativize(file.getParentFile().toURI()).getPath() + file.getName().substring(5);
					
					if ("file".equals(uri.getScheme()))
						file = resolveFile(template.getResourcesDirectory(), uri);
					else if ("http".equals(uri.getScheme()))
						file = getRepository().getFile(uri, monitor);
					else
						throw new IOException("Unsupported scheme uri: " + uri + " in file: " + file);
					
					assert(file != null);
				}
				
				boolean isTemplate = relativePath.endsWith(".gsp");
				if (isTemplate)
					relativePath = relativePath.substring(0, relativePath.length() - 4);
				
				File projectFile = new File(projectDir, relativePath);
				if (isTemplate) {
					monitor.setTaskName("Generating file: " + relativePath);
					GroovyTemplate.executeTemplate(file, projectFile, variables);
				}
				else {
					monitor.setTaskName("Copying file: " + relativePath);
					copyFile(file, projectFile);
					if (deleteAfterCopy)
						file.delete();
				}
				
				monitor.setWorkRemaining(80);
				monitor.worked(10);
			}
		}
	}
	
	protected void copyFile(File source, File target) throws IOException {
		InputStream in = null;
	    OutputStream out = null;
	    try {
			in = new FileInputStream(source);
		    out = new FileOutputStream(target);

		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0)
		        out.write(buf, 0, len);
	    }
	    finally {
		    in.close();
		    out.close();
	    }
	}
	
	protected String resolveVariables(String s, Map<String, Object> bindings) {
		boolean resolved = true;
		int iStart = -1, iEnd = -1;
		while ((iStart = s.indexOf("${")) != -1 && resolved) {
			resolved = false;
			iEnd = s.indexOf('}', iStart + 2);
			if (iEnd != -1) {
				String variable = s.substring(iStart + 2, iEnd);
				Object value = bindings.get(variable);
				if (value != null) {
					s = s.substring(0, iStart) + value + s.substring(iEnd + 1);
					resolved = true;
				}
			}
		}
		return s;
	}
	
	protected URI resolveUri(File uriFile, Map<String, Object> variables) throws IOException {
		try {
			GroovyShell shell = new GroovyShell(getClass().getClassLoader());
			Script script = shell.parse(uriFile);
			Script scriptInstance = InvokerHelper.createScript(script.getClass(), new Binding(new HashMap<String, Object>(variables)));
			scriptInstance.run();
			
			Object uri = scriptInstance.getBinding().getVariable("uri");
			if (uri == null)
				throw new IOException("Uri file: " + uriFile + " does not declare a \"uri\" variable");
			
			try {
				return new URI(uri.toString());
			}
			catch (URISyntaxException e) {
				throw new IOException("Uri file: " + uriFile + " declares an invalid \"uri\" variable: " + uri, e);
			}
	    }
	    catch (CancelFileGenerationException e) {
	    	return null;
	    }
	}
	
	protected File resolveFile(File parent, URI uri) throws FileNotFoundException {
		String path = uri.getSchemeSpecificPart();
		File file = new File(path);

		if (!file.isAbsolute())
			file = new File(parent, path);
		if (!file.exists())
			throw new FileNotFoundException(file.toString());
		
		return file;
	}
	
	public abstract Repository getRepository();

	public abstract boolean canFinish();
	
	public abstract boolean performFinish() throws CoreException, InterruptedException;
	
	public boolean performCancel() {
		return true;
	}
	
	/**
	 * For tests...
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String fileName = args.length > 0 ? args[0] : "C:\\workspace\\graniteds_wizard\\resources\\templates\\jboss\\bindings.groovy";
		File f = new File(fileName);
		Bindings bl = new Bindings(f);
		
		System.out.println("---------------------------");
		for (Variable variable : bl.getVariables()) {
			System.out.println(variable.getName() + " {");
			System.out.println("  label: " + variable.getLabel());
			System.out.println("  controlType: " + variable.getControlType());
			System.out.println("  possibleValues: " + variable.getPossibleValues());
			System.out.println("  value: " + variable.getValue());
			System.out.println("  type: " + variable.getType());
			System.out.println("  disabled: " + variable.isDisabled());
			System.out.println("  validate: " + variable.validate());
			System.out.println("  errorMessage: " + variable.getErrorMessage());
			System.out.println("}");
		}

		System.out.println("---------------------------");
		System.out.println(bl.getBindingMap());
	}
}
