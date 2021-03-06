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

package org.granite.wizard.repository;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.runtime.SubMonitor;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;

/**
 * @author Franck WOLFF
 */
public interface Repository {

	public void initialize(DynamicProjectWizard wizard, ProjectTemplate template) throws IOException;
	
	File getFile(URI uri) throws IOException;
	File getFile(URI uri, SubMonitor monitor) throws IOException;
	boolean clearFile(URI uri);
	void clearAllFile();
}
