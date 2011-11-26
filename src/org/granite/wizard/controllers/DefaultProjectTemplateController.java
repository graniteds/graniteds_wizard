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

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.granite.wizard.DynamicProjectWizard;
import org.granite.wizard.ProjectTemplate;
import org.granite.wizard.WizardException;
import org.granite.wizard.bindings.Bindings;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.bindings.VariableChangeEvent;
import org.granite.wizard.repository.DefaultRepository;
import org.granite.wizard.repository.Repository;

/**
 * @author Franck WOLFF
 */
public class DefaultProjectTemplateController extends AbstractTemplateController {

	private Repository repository;
	private WizardNewProjectCreationPage projectPage;
	private Bindings bindings;

	@Override
	public Repository getRepository() {
		return repository;
	}
	
	protected void initializeRepository() {
		repository = new DefaultRepository();
		try {
			repository.initialize(wizard, template);
		} catch (Exception e) {
			throw new WizardException("Failed to initialize repository", e);
		}
	}
	
	protected void initializeBindings() {
		try {
			bindings = getTemplate().getBindings();
		}
		catch (Exception e) {
			throw new WizardException("Failed to load bindings file: " + getTemplate().getDirectory(), e);
		}
	}

	@Override
	public void initialize(final DynamicProjectWizard wizard, ProjectTemplate template) {
		super.initialize(wizard, template);

		initializeRepository();
		initializeBindings();
		
		projectPage = new WizardNewProjectCreationPage(getClass().getSimpleName()) {

			@Override
			public void createControl(Composite parent) {
				super.createControl(parent);
				parent = (Composite)getControl();
				
				final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
				group.setText("Project Configuration:");
				group.setLayout(new GridLayout(1, false));
				group.setLayoutData(new GridData(GridData.FILL_BOTH));
				
		        final ScrolledComposite scrolled = new ScrolledComposite(group, SWT.V_SCROLL);
		        scrolled.setExpandHorizontal(true);
		        scrolled.setExpandVertical(true);
		        scrolled.getVerticalBar().setIncrement(20);
		        scrolled.setMinSize(0, 0);
		        scrolled.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		 
		        final Composite composite = new Composite(scrolled, SWT.NONE);
		        GridLayout layout = new GridLayout();
		        layout.numColumns = 2;
		        layout.makeColumnsEqualWidth = false;
		        layout.horizontalSpacing = 10;
		        composite.setLayout(layout);
		 
		        for (Variable variable : bindings.getVariables()) {
		        	String labelText = variable.getLabel();
		        	if (labelText != null) {
			            Label label = new Label(composite, SWT.NONE);
			            label.setFont(composite.getFont());
			            if (labelText.startsWith("|- "))
			            	labelText = "  \u21b3 " + labelText.substring(2);
			            label.setText(labelText + ":");
			            label.setToolTipText(variable.getTooltip());
			            
			            variable.createControl(composite, label, 200);
		        	}
		        }

		        composite.addListener(VariableChangeEvent.ID, new Listener() {
					@Override
					public void handleEvent(Event e) {
						// Re-dispatch change event to all controls so they can update their
						// values.
						for (Control control : ((Composite)e.widget).getChildren())
							control.notifyListeners(VariableChangeEvent.ID, e);
						
						// Validate all controls and update "Finish" button state.
						boolean valid = validatePage();
						setPageComplete(valid);
					}
				});
		        
		        composite.addControlListener(new ControlAdapter() {
		            public void controlResized(ControlEvent e) {
		                Point s = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		                scrolled.setMinSize(s);
		            }
		        });
		        
		        Point s = new Point(400, 300);
		        composite.setSize(s);
		        scrolled.setMinSize(s);
		        scrolled.setContent(composite);

		        final Button saveAsDefault = new Button(parent, SWT.PUSH);
		        saveAsDefault.setText("Save as default");
		        saveAsDefault.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						bindings.saveDefaultProperties();
					}
				});
			}

			@Override
			protected boolean validatePage() {
				boolean valid = super.validatePage();
				if (valid) {
					for (Variable variable : bindings.getVariables()) {
						String message = variable.validate();
						if (message != null) {
							String labelText = variable.getLabel();
				            if (labelText.startsWith("|- "))
				            	labelText = labelText.substring(2);
							setErrorMessage(labelText + ": " + message);
							valid = false;
							break;
						}
					}
				}
				return valid;
			}
		};
		projectPage.setInitialProjectName("");
		projectPage.setTitle(template.getTitle());
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
		
		if (!canFinish())
			return false;
		
		final Map<String, Object> variables = bindings.getBindingMap();
		variables.put("projectName", projectPage.getProjectName());
		
		return performFinish(variables, projectPage);

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
	}

	@Override
	public boolean performCancel() {
		return true;
	}
}
