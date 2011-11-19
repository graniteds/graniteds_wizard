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

import java.util.Arrays;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.bindings.VariableChangeEvent;

/**
 * @author Franck WOLFF
 */
public class ComboControl extends AbstractControl<Combo> {

	public ComboControl(Variable variable) {
		super(variable);
	}

	@Override
	protected Combo internalCreateControl(Composite parent, int widthHint) {
		
		final Combo combo = new Combo(parent, SWT.READ_ONLY);
		Map<String, String> items = variable.getPossibleValues();
		String[] labels = items.keySet().toArray(new String[0]);
		String[] values = items.values().toArray(new String[0]);
		combo.setItems(labels);
		combo.select(Arrays.binarySearch(values, variable.getValueAsString()));
		combo.setEnabled(!variable.isDisabled());
		
		final SelectionAdapter modifyListener = new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				variable.setValue(variable.getPossibleValues().get(combo.getText()));
                combo.getParent().notifyListeners(VariableChangeEvent.ID, new VariableChangeEvent(combo));
            }
        };
		combo.addSelectionListener(modifyListener);
		
		combo.addListener(VariableChangeEvent.ID, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (((VariableChangeEvent)e).source == combo)
					return;
				
				combo.setEnabled(!variable.isDisabled());
				
				Map<String, String> items = variable.getPossibleValues();
				String[] labels = items.keySet().toArray(new String[0]);
				if (!Arrays.equals(combo.getItems(), labels)) {
					combo.removeSelectionListener(modifyListener);
					combo.setItems(labels);
					combo.addSelectionListener(modifyListener);
				}

				String value = variable.getValueAsString();
				if (!items.get(combo.getText()).equals(value)) {
					String[] values = items.values().toArray(new String[0]);
					combo.removeSelectionListener(modifyListener);
					combo.select(Arrays.binarySearch(values, variable.getValueAsString()));
					combo.addSelectionListener(modifyListener);
				}
			}
		});

		return combo;
	}
}
