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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import org.granite.generator.ScriptingEngine;
import org.granite.wizard.bindings.Bindings;
import org.granite.wizard.controllers.AbstractTemplateController;
import org.granite.wizard.controllers.DefaultProjectTemplateController;
import org.granite.wizard.util.PropertiesUtil;

/**
 * @author Franck WOLFF
 */
public class ProjectTemplate {

	public static final String BINDINGS_GROOVY = "bindings.groovy";
	public static final String TEMPLATE_PROPERTIES = "template.properties";

	private static final String IGNORE_FILES = "ignoreFiles";

	private static final String NAME = "name";
	private static final String CONTROLLER = "controller";	
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	
	private final File directory;
	private final Properties properties;
	private final ScriptingEngine engine;
	private final String name;
	private final Class<? extends AbstractTemplateController> controller;
	private final Pattern ignoreFiles;
	
	private Bindings bindings;
	
	@SuppressWarnings("unchecked")
	public ProjectTemplate(File directory, Properties globalProperties, ScriptingEngine engine) {
		this.directory = directory;
		
		String ignoreFiles = globalProperties.getProperty(IGNORE_FILES);
		this.ignoreFiles = Pattern.compile((ignoreFiles != null ? ignoreFiles : "^$"));
		
		this.engine = engine;
		
		try {
			this.properties = PropertiesUtil.loadProperties(directory, TEMPLATE_PROPERTIES, true);
		}
		catch (IOException e) {
			throw new ProjectTemplateException("Could not load template properties", e);
		}
		
		String name = properties.getProperty(NAME);
		if (name == null || name.length() == 0)
			throw new ProjectTemplateException("No or empty template name in: " + directory + "/" + TEMPLATE_PROPERTIES);
		this.name = name.trim();
		
		String controllerClassName = properties.getProperty(CONTROLLER);
		if (controllerClassName == null || controllerClassName.length() == 0)
			controllerClassName = DefaultProjectTemplateController.class.getName();
		else
			controllerClassName = controllerClassName.trim();

		try {
			this.controller = (Class<? extends AbstractTemplateController>)getClass().getClassLoader().loadClass(controllerClassName);
		}
		catch (Exception e) {
			throw new ProjectTemplateException("Cannot load controller class '" + controllerClassName + "' in: " + directory + "/" + TEMPLATE_PROPERTIES);
		}
	}

	public File getDirectory() {
		return directory;
	}
	
	public File[] getProjectDirectories() {
		File[] files = directory.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		
		return (files.length > 0 ? files : null);
	}
	
	public File getResourcesDirectory() {
		if (directory.getParentFile() != null && directory.getParentFile().getParentFile() != null)
			return directory.getParentFile().getParentFile();
		return null;
	}

	public Properties getProperties() {
		return properties;
	}

	public ScriptingEngine getEngine() {
		return engine;
	}

	public File getBindingsFile() throws IOException {
		File file = new File(directory, BINDINGS_GROOVY);
		if (!file.exists())
			throw new FileNotFoundException("File doesn't exist: " + file);
		if (!file.isFile())
			throw new IOException("Not a regular file: " + file);
		return file;
	}

	public Bindings getBindings() throws IOException {
		if (bindings == null)
			bindings = new Bindings(getBindingsFile(), engine);
		return bindings;
	}

	public String getName() {
		return name;
	}

	public Class<? extends AbstractTemplateController> getController() {
		return controller;
	}
	
	public String getTitle() {
		String title = properties.getProperty(TITLE);
		return (title != null ? title.trim() : name);
	}
	
	public String getDescription() {
		String description = properties.getProperty(DESCRIPTION);
		return (description != null ? description.trim() : "");
	}
	
	public boolean ignoreFile(File file) {
		return (ignoreFiles != null ? ignoreFiles.matcher(file.getName()).matches() : false);
	}
}
