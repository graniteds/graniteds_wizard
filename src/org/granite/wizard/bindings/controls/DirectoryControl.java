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

package org.granite.wizard.bindings.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.granite.wizard.bindings.ValidationException;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.bindings.VariableChangeEvent;

/**
 * @author Franck WOLFF
 */
public class DirectoryControl extends AbstractControl<Composite> {

	private Text text;
	
	public DirectoryControl(Variable variable) {
		super(variable);
	}

	@Override
	protected Composite internalCreateControl(Composite parent) {
		
		final Composite box = new Composite(parent, SWT.NONE);
		GridLayout boxLayout = new GridLayout(2, false);
		boxLayout.horizontalSpacing = 4;
		boxLayout.marginWidth = 0;
		boxLayout.marginBottom = 0;
		boxLayout.marginHeight = 0;
		boxLayout.marginLeft = 0;
		boxLayout.marginRight = 0;
		boxLayout.marginTop = 0;
		box.setLayout(boxLayout);
		
		boolean enabled = !variable.isDisabled();
		
		text = new Text(box, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		text.setText(variable.getValueAsString());
		text.setEnabled(enabled);
		final ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				variable.setValue(text.getText());
				box.getParent().notifyListeners(VariableChangeEvent.ID, new VariableChangeEvent(box));
			}
		};
		text.addModifyListener(modifyListener);
		
		final Button button = new Button(box, SWT.PUSH);
		button.setText("Browse...");
		button.setEnabled(enabled);
		
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(button.getShell());				
				dialog.setFilterPath(text.getText());
				dialog.setText("Directory Chooser");
				dialog.setMessage(variable.getLabel());
				
				String value = dialog.open();
				if (value != null && !value.equals(text.getText()))
					text.setText(value);
			}
		});

		
		box.addListener(VariableChangeEvent.ID, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (((VariableChangeEvent)e).source == box)
					return;				
				
				boolean enabled = !variable.isDisabled();
				text.setEnabled(enabled);
				button.setEnabled(enabled);
				
				String value = variable.getValueAsString();
				if (!text.getText().equals(value)) {
					text.removeModifyListener(modifyListener);
					text.setText(value);
					text.addModifyListener(modifyListener);
				}
			}
		});

		return box;
	}

	@Override
	public void displayError(ValidationException e) {
		text.setBackground(ERROR_COLOR);
		text.setToolTipText(e.getMessage());
	}

	@Override
	public void resetError() {
		text.setBackground(background);
		text.setToolTipText("");
	}
}
