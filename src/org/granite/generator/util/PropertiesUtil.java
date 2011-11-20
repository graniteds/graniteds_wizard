package org.granite.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.granite.wizard.ProjectTemplateException;

public class PropertiesUtil {
	
	public static Properties loadProperties(final File directory, final String propertyFileName) {
		if (!directory.isDirectory())
			throw new ProjectTemplateException("Not a directory: " + directory);
		
		File propertiesFile = new File(directory, propertyFileName);
		if (!propertiesFile.exists())
			throw new ProjectTemplateException("No " + propertyFileName + " file in directory: " + directory);
		if (!propertiesFile.isFile())
			throw new ProjectTemplateException("Invalid " + propertyFileName + " file in directory: " + directory);
		
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(propertiesFile);
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
	
	public static void saveProperties(final Properties properties, final File directory, final String propertyFileName) {
		File propertiesFile = new File(directory, propertyFileName);
		if (propertiesFile.exists() && !propertiesFile.isFile())
			throw new ProjectTemplateException("Invalid " + propertyFileName + " file in directory: " + directory);
		OutputStream os = null;
		try {
			os = new FileOutputStream(propertiesFile);
			properties.store(os, null);
		}
		catch (IOException e) {
			throw new ProjectTemplateException("Cannot save " + propertyFileName + " file in directory: " + directory, e);
		}
		finally {
			if (os != null) try {
				os.close();
			} catch (Exception e) {
				// ignore...
			}
		}
	}
}
