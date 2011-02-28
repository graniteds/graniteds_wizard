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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.granite.generator.gsp.GroovyTemplate;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;
import org.granite.wizard.bindings.Bindings;
import org.granite.wizard.bindings.Variable;

/**
 * @author Franck WOLFF
 */
public abstract class AbstractTemplateController {

	private DynamicProjectWizard wizard;
	private ProjectTemplate template;
	private IWizardPage nextPage;

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
	
	public void createProjectResources(IProject project, Bindings bindings, SubMonitor monitor) throws IOException {
		File projectDir = new File(project.getLocationURI());
		Map<String, Object> variables = bindings.getBindingMap();
		variables.put("projectName", project.getName());
		createProjectResources(projectDir, template.getProjectDirectory().toURI(), template.getProjectDirectory(), monitor, variables);
	}
	
	protected void createProjectResources(File projectDir, URI projectTemplateURI, File directory, SubMonitor monitor, Map<String, Object> variables) throws IOException {
		for (File file : directory.listFiles()) {
			
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
				boolean isReference = file.getName().startsWith("@");
				if (isReference) {
					String resolved = file.getName().substring(1).replace('!', '/');
					File resolvedFile = new File(template.getResourcesDirectory(), resolved);
					relativePath = projectTemplateURI.relativize(file.getParentFile().toURI()).getPath() + resolvedFile.getName();
					file = resolvedFile;
				}
				
				boolean isTemplate = relativePath.endsWith(".gsp");
				if (isTemplate) {
					relativePath = relativePath.substring(0, relativePath.length() - 4);
				}
				
				File projectFile = new File(projectDir, relativePath);
				if (isTemplate) {
					monitor.setTaskName("Generating file: " + relativePath);
					GroovyTemplate.executeTemplate(file, projectFile, variables);
				}
				else {
					monitor.setTaskName("Copying file: " + relativePath);
					copyFile(file, projectFile);
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
