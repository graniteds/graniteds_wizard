package org.granite.wizard.repository;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.runtime.SubMonitor;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;

public interface Repository {

	public void initialize(DynamicProjectWizard wizard, ProjectTemplate template) throws IOException;
	
	File getFile(URI uri) throws IOException;
	File getFile(URI uri, SubMonitor monitor) throws IOException;
	boolean clearFile(URI uri);
	void clearAllFile();
}
