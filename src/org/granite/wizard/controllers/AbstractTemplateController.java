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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.granite.generator.CancelFileGenerationException;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;
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
		monitor.setWorkRemaining(200);
		createProjectResources(projectDir, projectTemplateDirectory.toURI(), projectTemplateDirectory, monitor, variables);
		monitor.done();
	}
	
	protected void createProjectResources(File projectDir, URI projectTemplateURI, File directory, SubMonitor monitor, Map<String, Object> variables) throws IOException {
		
		monitor.setTaskName("Creating content from " + directory + " directory...");
		
		for (File file : directory.listFiles()) {
			
			if (template.ignoreFile(file))
				continue;
			
			String relativePath = projectTemplateURI.relativize(file.toURI()).getPath();
			if (relativePath.contains("${"))
				relativePath = resolveVariables(relativePath, variables);
			
			if (file.isDirectory()) {				
				File projectFile = new File(projectDir, relativePath);
				monitor.setTaskName("Creating directory: " + relativePath);
				projectFile.mkdirs();
				monitor.worked(1);
				createProjectResources(projectDir, projectTemplateURI, file, monitor, variables);
			}
			else {
				if (file.getName().startsWith("@uri:")) {
					relativePath = projectTemplateURI.relativize(file.getParentFile().toURI()).getPath() + file.getName().substring(5);
					file = resolveUri(file, variables, monitor.newChild(10));
					if (file == null) // CancelFileGenerationException.
						continue;
				}
				
				boolean isTemplate = relativePath.endsWith(".gsp");
				if (isTemplate)
					relativePath = relativePath.substring(0, relativePath.length() - 4);
				
				File projectFile = new File(projectDir, relativePath);
				if (isTemplate) {
					monitor.setTaskName("Generating file: " + relativePath);
					template.getEngine().execute(file, projectFile, variables);
				}
				else {
					monitor.setTaskName("Copying file: " + relativePath);
					copyFile(file, projectFile);
				}
				
				monitor.worked(5);
			}
		}
	}
	
	protected void copyFile(File source, File target) throws IOException {
		if (target.getParentFile() != null && !target.getParentFile().exists())
			target.getParentFile().mkdirs();
		
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
	
	protected String resolveVariables(String s, Map<String, Object> variables) {
		return template.getEngine().evaluate(s, variables);
	}
	
	protected File resolveUri(File uriFile, Map<String, Object> variables, SubMonitor monitor) throws IOException {
		File file = null;
		
		monitor.setWorkRemaining(100);
		
		Object uriVariable = null;
		try {
			uriVariable = template.getEngine().evaluate(uriFile, variables).get("uri");
			if (uriVariable == null)
				throw new IOException("Uri file: " + uriFile + " does not declare a \"uri\" variable");
			
			monitor.worked(25);
			
			URI uri = new URI(uriVariable.toString());
			
			if ("mvn".equals(uri.getScheme()))
				file = getRepository().getFile(mvn2Http(uri), monitor.newChild(75));
			else if ("http".equals(uri.getScheme()))
				file = getRepository().getFile(uri, monitor.newChild(75));
			else if ("file".equals(uri.getScheme()))
				file = resolveFile(template.getResourcesDirectory(), uri);
			else
				throw new IOException("Unsupported scheme uri: " + uri + " in file: " + file);
	    }
	    catch (CancelFileGenerationException e) {
	    	file = null;
	    }
		catch (URISyntaxException e) {
			throw new IOException("Uri file: " + uriFile + " declares an invalid \"uri\" variable: " + uriVariable, e);
		}
		
		monitor.done();
		
		return file;
	}
	
	protected URI mvn2Http(URI mvnUri) {
		
		String repository = (String)template.getEngine().getGlobalVariable("DEFAULT_MAVEN_REPOSITORY");
		if (repository == null)
			repository = "http://repo2.maven.org/maven2/";
		
		String coordinate = mvnUri.getRawSchemeSpecificPart();
		int repositoryEnd = coordinate.indexOf('!');
		if (repositoryEnd != -1) {
			repository = coordinate.substring(0, repositoryEnd);
			coordinate = coordinate.substring(repositoryEnd + 1);
		}
		
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
	
//	/**
//	 * For tests...
//	 * 
//	 * @param args
//	 * @throws Exception
//	 */
//	public static void main(String[] args) throws Exception {
//		String fileName = args.length > 0 ? args[0] : "C:\\workspace\\graniteds_wizard\\resources\\templates\\jboss\\bindings.groovy";
//		File f = new File(fileName);
//		Bindings bl = new Bindings(f);
//		
//		System.out.println("---------------------------");
//		for (Variable variable : bl.getVariables()) {
//			System.out.println(variable.getName() + " {");
//			System.out.println("  label: " + variable.getLabel());
//			System.out.println("  controlType: " + variable.getControlType());
//			System.out.println("  possibleValues: " + variable.getPossibleValues());
//			System.out.println("  value: " + variable.getValue());
//			System.out.println("  type: " + variable.getType());
//			System.out.println("  disabled: " + variable.isDisabled());
//			System.out.println("  validate: " + variable.validate());
//			System.out.println("  errorMessage: " + variable.getErrorMessage());
//			System.out.println("}");
//		}
//
//		System.out.println("---------------------------");
//		System.out.println(bl.getBindingMap());
//	}
}
