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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.granite.wizard.Activator;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;
import org.granite.wizard.WizardException;
import org.granite.wizard.bindings.Bindings;
import org.granite.wizard.bindings.ValidationEvent;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.bindings.VariableChangeEvent;

/**
 * @author Franck WOLFF
 */
public class BasicProjectTemplateController extends AbstractTemplateController {

	private WizardNewProjectCreationPage projectPage;
	private Bindings bindings;

	@Override
	public void initialize(final DynamicProjectWizard wizard, ProjectTemplate template) {
		super.initialize(wizard, template);
		
		try {
			bindings = new Bindings(getTemplate().getBindingsFile());
		}
		catch (Exception e) {
			throw new WizardException("Failed to load: " + getTemplate().getBindingsFile(), e);
		}
		
		projectPage = new WizardNewProjectCreationPage(getClass().getSimpleName()) {

			@Override
			public void createControl(Composite parent) {
				
				super.createControl(parent);
				
				Composite control = (Composite)getControl();
				
				boolean hasWorkingSetGroup = false;
				IWorkingSet[] workingSets = wizard.getWorkbench().getWorkingSetManager().getWorkingSets();
				if (workingSets != null && workingSets.length > 0) {
					String[] workingSetIds = new String[workingSets.length];
					for (int i = 0; i < workingSets.length; i++)
						workingSetIds[i] = workingSets[i].getId();
					createWorkingSetGroup(control, wizard.getSelection(), workingSetIds);
					hasWorkingSetGroup = true;
				}
				
				final Group group = new Group(control, SWT.NONE);
				group.setText("Template variables");
				group.setLayout(new GridLayout(1, false));
				group.setLayoutData(new GridData(GridData.FILL_BOTH));
				
		        final ScrolledComposite sc = new ScrolledComposite(group, SWT.V_SCROLL);
		        sc.setExpandHorizontal(true);
		        sc.setExpandVertical(true);
		        sc.getVerticalBar().setIncrement(20);
		        sc.setMinSize(0, 0);
		        GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
				data.heightHint = (hasWorkingSetGroup ? 300 : 400);
		        sc.setLayoutData(data);
		 
		        final Composite composite = new Composite(sc, SWT.NONE);
		        GridLayout layout = new GridLayout();
		        layout.numColumns = 2;
		        layout.makeColumnsEqualWidth = false;
		        layout.horizontalSpacing = 10;
		        composite.setLayout(layout);

		        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		        GC gc = new GC(composite);
		        gc.setFont(composite.getFont());
		        gridData.heightHint = gc.stringExtent("A").y + 4;
		        gc.dispose();
		        
		        final Listener validationListener = new Listener() {
					@Override
					public void handleEvent(Event e) {
						ValidationEvent ve = (ValidationEvent)e;
						if (isPageComplete() && ve.exception != null)
							setErrorMessage("At least one template variable has an invalid value (see below)");
					}
				};
		 
		        for (Variable variable : bindings.getVariables()) {
		        	variable.setValidationListener(validationListener);
		        	
		        	String labelText = variable.getLabel();
		        	if (labelText != null) {
			            Label label = new Label(composite, SWT.NONE);
			            label.setFont(composite.getFont());
			            label.setText(labelText + ":");
			            
			            Control variableControl = variable.createControl(composite);
			            variableControl.setFont(composite.getFont());
			            variableControl.setLayoutData(gridData);
		        	}
		        }

		        composite.addListener(VariableChangeEvent.ID, new Listener() {
					@Override
					public void handleEvent(Event e) {
						if (isPageComplete())
							setErrorMessage(null);
						for (Control control : ((Composite)e.widget).getChildren())
							control.notifyListeners(VariableChangeEvent.ID, e);
					}
				});
		        
		        composite.addControlListener(new ControlAdapter() {
		            public void controlResized(ControlEvent e) {
		                Point s = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		                sc.setMinSize(s);
		            }
		        });
		        
		        sc.setContent(composite);
		        Point s = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		        sc.setMinSize(s);
			}
		};
		projectPage.setInitialProjectName("");
		projectPage.setTitle(template.getTitle());
		projectPage.setDescription(template.getDescription());
		projectPage.setWizard(getWizard());
		projectPage.setPreviousPage(getStartingPage());
		
		setNextPage(projectPage);
	}

	@Override
	public boolean canFinish() {
		return projectPage != null && projectPage.isPageComplete();
	}

	@Override
	public boolean performFinish() throws CoreException, InterruptedException {
		
		boolean canFinish = canFinish();
		for (Variable variable : bindings.getVariables())
			canFinish = variable.validate() && canFinish;
		if (!canFinish)
			return false;
		
		final String projectName = projectPage.getProjectName();
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	    final IWorkingSet[] workingSets = projectPage.getSelectedWorkingSets();
	    
		Job job = new Job("Creating '" + projectName + "' project...") {
			public IStatus run(IProgressMonitor monitor) {
				
				try {
					SubMonitor sm = SubMonitor.convert(monitor);

					// create the project.
					sm.setTaskName("Creating project...");
					sm.setWorkRemaining(100);
					project.create(sm.newChild(1));
					project.open(sm.newChild(1));

					// delete the .project file (must be in the template).
					IFile projectFile = project.getFile(".project");
					projectFile.delete(true, false, sm.newChild(1));
					
					// create all resources for the project (including a new .project file).
					createProjectResources(project, bindings, sm);
					
					// refresh project resources.
					sm.setTaskName("Refreshing project...");
					sm.setWorkRemaining(10);
					project.refreshLocal(IResource.DEPTH_INFINITE, sm.newChild(10));

					// add project to selected workingsets.
					if (workingSets != null && workingSets.length > 0)
						PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(project, workingSets);

				}
				catch(CoreException e) {
					return e.getStatus();
				}
				catch(Exception e) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not create project '" + projectName + "'", e);
				}
				
				return Status.OK_STATUS;
	        }
		};
	    
		job.setRule(project);
	    job.schedule();

//		IExtensionRegistry reg = RegistryFactory.getRegistry();
//		IExtension fb = reg.getExtension("com.adobe.flexbuilder.project.flexbuilder");
//		Bundle b = Platform.getBundle("org.granite.builder");
//		System.out.println(b.getVersion());
//				
//		b = Platform.getBundle("com.adobe.flexbuilder.project");
//		System.out.println(b.getVersion().getMajor());
//		System.out.println(b.getVersion().getMinor());
//		System.out.println(b.getVersion().getMicro());
//		System.out.println(b.getVersion().getQualifier());
//				
//		b = Platform.getBundle("org.jboss.ide.eclipse.archives.core");
//		System.out.println(b);
//		IWorkbenchPart p = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
//			
//		IViewDescriptor ivd = PlatformUI.getWorkbench().getViewRegistry().find("org.jboss.ide.eclipse.archives.ui.ProjectArchivesView");
//		System.out.println(ivd);
//		try {
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.jboss.ide.eclipse.archives.ui.ProjectArchivesView");
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
//			
//		p.setFocus();

	    return true;
	}

	@Override
	public boolean performCancel() {
		return true;
	}
}
