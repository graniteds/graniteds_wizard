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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.bindings.VariableChangeEvent;

/**
 * @author Franck WOLFF
 */
public class CheckboxControl extends AbstractControl<Button> {

	public CheckboxControl(Variable variable) {
		super(variable);
	}

	@Override
	protected Button internalCreateControl(Composite parent, int widthHint) {
		
		final Button checkbox = new Button(parent, SWT.CHECK);
		checkbox.setSelection(variable.getValueAsBoolean());
		checkbox.setEnabled(!variable.isDisabled());
		
		final SelectionAdapter modifyListener = new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				variable.setValue(checkbox.getSelection());
                checkbox.getParent().notifyListeners(VariableChangeEvent.ID, new VariableChangeEvent(checkbox));
            }
        };
		checkbox.addSelectionListener(modifyListener);
		
		checkbox.addListener(VariableChangeEvent.ID, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (((VariableChangeEvent)e).source == checkbox)
					return;
				
				checkbox.setEnabled(!variable.isDisabled());
				boolean value = variable.getValueAsBoolean();
				if (checkbox.getSelection() != value) {
					checkbox.removeSelectionListener(modifyListener);
					checkbox.setSelection(value);
					checkbox.addSelectionListener(modifyListener);
				}
			}
		});

		return checkbox;
	}
}
