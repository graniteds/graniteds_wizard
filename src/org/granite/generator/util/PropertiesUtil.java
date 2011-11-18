package org.granite.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.granite.wizard.ProjectTemplateException;

public class PropertiesUtil {
	
	public static Properties loadProperties(final File directory, final String propertyFileName) {
		if (!directory.isDirectory())
			throw new ProjectTemplateException("Not a directory: " + directory);
		
		File[] propertiesList = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return propertyFileName.equals(name);
			}
		});
		
		if (propertiesList.length == 0)
			throw new ProjectTemplateException("No " + propertyFileName + " file in directory: " + directory);
		
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(propertiesList[0]);
			properties.load(is);
		}
		catch (IOException e) {
			throw new ProjectTemplateException("Cannot load " + propertyFileName + " file in directory: " + directory, e);
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

}
