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

package org.granite.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.granite.wizard.controllers.AbstractTemplateController;

/**
 * @author Franck WOLFF
 */
public class ProjectTemplate {

	private static final String BINDINGS_GROOVY = "bindings.groovy";
	private static final String TEMPLATE_PROPERTIES = "template.properties";
	private static final String PROJECT = "project";
	private static final String NAME = "name";
	private static final String CONTROLLER = "controller";	
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	
	private final File directory;
	private final Properties properties;
	private final String name;
	private final Class<? extends AbstractTemplateController> controller;
	
	@SuppressWarnings("unchecked")
	public ProjectTemplate(File directory) {
		this.directory = directory;
		this.properties = loadTemplateProperties();
		
		this.name = properties.getProperty(NAME);
		if (name == null || name.length() == 0)
			throw new ProjectTemplateException("No or empty template name in: " + directory + "/" + TEMPLATE_PROPERTIES);
		
		String controllerClassName = properties.getProperty(CONTROLLER);
		if (name == null || name.length() == 0)
			throw new ProjectTemplateException("No or empty template controller in: " + directory + "/" + TEMPLATE_PROPERTIES);

		try {
			this.controller = (Class<? extends AbstractTemplateController>)getClass().getClassLoader().loadClass(controllerClassName);
		}
		catch (Exception e) {
			throw new ProjectTemplateException("Cannot load controller class '" + controllerClassName + "' in: " + directory + "/" + TEMPLATE_PROPERTIES);
		}
	}
	
	private Properties loadTemplateProperties() {
		if (!directory.isDirectory())
			throw new ProjectTemplateException("Not a directory: " + directory);
		
		File[] propertiesList = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return TEMPLATE_PROPERTIES.equals(name);
			}
		});
		
		if (propertiesList.length == 0)
			throw new ProjectTemplateException("No " + TEMPLATE_PROPERTIES + " file in template directory: " + directory);
		
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(propertiesList[0]);
			properties.load(is);
		}
		catch (IOException e) {
			throw new ProjectTemplateException("Cannot load " + TEMPLATE_PROPERTIES + " file in template directory: " + directory, e);
		}
		finally {
			if (is != null) try {
				is.close();
			} catch (Exception e) {
				// ignore...
			}
		}
		
		return properties;
	}

	public File getDirectory() {
		return directory;
	}
	
	public File getProjectDirectory() {
		File[] files = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return PROJECT.equals(name);
			}
		});
		
		if (files.length > 0 && files[0].isDirectory())
			return files[0];
		return null;
	}
	
	public File getResourcesDirectory() {
		if (directory.getParentFile() != null && directory.getParentFile().getParentFile() != null)
			return directory.getParentFile().getParentFile();
		return null;
	}

	public Properties getProperties() {
		return properties;
	}

	public File getBindingsFile() {
		
		File[] bindingsList = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return BINDINGS_GROOVY.equals(name);
			}
		});
		
		if (bindingsList.length == 0)
			return null;
		
		return bindingsList[0];
	}

	public String getName() {
		return name;
	}

	public Class<? extends AbstractTemplateController> getController() {
		return controller;
	}
	
	public String getTitle() {
		String title = properties.getProperty(TITLE);
		return (title != null ? title : name);
	}
	
	public String getDescription() {
		String description = properties.getProperty(DESCRIPTION);
		return (description != null ? description : "");
	}
}
