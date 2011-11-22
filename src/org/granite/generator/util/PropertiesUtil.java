package org.granite.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties loadProperties(File directory, String propertiesFileName) throws IOException {
		return loadProperties(new File(directory, propertiesFileName), false);
	}

	public static Properties loadProperties(File directory, String propertiesFileName, boolean fromXML) throws IOException {
		return loadProperties(new File(directory, propertiesFileName), fromXML);
	}

	public static Properties loadProperties(File propertiesFile) throws IOException {
		return loadProperties(propertiesFile, false);
	}

	public static Properties loadProperties(File propertiesFile, boolean fromXML) throws IOException {
		if (!propertiesFile.exists())
			throw new FileNotFoundException("\"" + propertiesFile + "\" does not exists");
		if (!propertiesFile.isFile())
			throw new IOException("\"" + propertiesFile + "\" is not a regular file");
		
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(propertiesFile);
			if (fromXML)
				properties.loadFromXML(is);
			else
				properties.load(is);
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

	public static void storeProperties(Properties properties, File directory, String propertiesFileName) throws IOException {
		storeProperties(properties, new File(directory, propertiesFileName), false);
	}

	public static void storeProperties(Properties properties, File directory, String propertiesFileName, boolean toXML) throws IOException {
		storeProperties(properties, new File(directory, propertiesFileName), toXML);
	}

	public static void storeProperties(Properties properties, File propertiesFile) throws IOException {
		storeProperties(properties, propertiesFile, false);
	}

	public static void storeProperties(Properties properties, File propertiesFile, boolean toXML) throws IOException {
		if (propertiesFile.exists() && !propertiesFile.isFile())
			throw new IOException("\"" + propertiesFile + "\" is not a regular file");

		OutputStream os = null;
		try {
			os = new FileOutputStream(propertiesFile);
			if (toXML)
				properties.storeToXML(os, null);
			else
				properties.store(os, null);
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
