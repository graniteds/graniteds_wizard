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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
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
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = widthHint;
		combo.setLayoutData(gridData);
		
		Map<String, String> items = checkNull(variable.getPossibleValues());
		String[] labels = items.keySet().toArray(new String[0]);
		String[] values = items.values().toArray(new String[0]);
		
		combo.setItems(labels);
		
		if (labels.length == 0)
			variable.setValue(null);
		else {
			int selected = indexOf(values, variable.getValueAsString());
			if (selected == -1) {
				combo.select(0);
				variable.setValue(values[0]);
			}
			else
				combo.select(selected);
		}

		final boolean enabled = (labels.length > 0 && !variable.isDisabled());
		combo.setEnabled(enabled);
		setLabelEnabled(enabled);
		
		final SelectionAdapter modifyListener = new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				String label = combo.getText();
				if (label != null) {
					String value = variable.getPossibleValues().get(label);
					if (value != null && !value.equals(variable.getValue())) {
						variable.setValue(value);
		                combo.getParent().notifyListeners(VariableChangeEvent.ID, new VariableChangeEvent(combo));
					}
				}
            }
        };
		combo.addSelectionListener(modifyListener);
		
		combo.addListener(VariableChangeEvent.ID, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (((VariableChangeEvent)e).source == combo)
					return;
				
				Map<String, String> items = checkNull(variable.getPossibleValues());
				String[] labels = items.keySet().toArray(new String[0]);				
				String[] values = items.values().toArray(new String[0]);
				
				combo.removeSelectionListener(modifyListener);

				boolean valueChanged = false;
				
				// Items list has changed.
				if (!Arrays.equals(combo.getItems(), labels)) {
					combo.setItems(labels);
					combo.select(0);
				}
				
				// Update value and selection.
				String value = variable.getValueAsString();
				if (value == null) {
					if (values.length > 0) {
						variable.setValue(values[0]);
						valueChanged = true;
					}
				}
				else if (values.length == 0) {
					variable.setValue(null);
					valueChanged = true;
				}
				else if (!value.equals(values[combo.getSelectionIndex()])) {
					int selected = indexOf(values, value);
					if (selected == -1) {
						variable.setValue(values[0]);
						valueChanged = true;
					}
					else
						combo.select(selected);
				}

				final boolean enabled = (labels.length > 0 && !variable.isDisabled());
				combo.setEnabled(enabled);
				setLabelEnabled(enabled);

				combo.addSelectionListener(modifyListener);
				
				if (valueChanged)
					combo.getParent().notifyListeners(VariableChangeEvent.ID, new VariableChangeEvent(combo, ((VariableChangeEvent)e).recursion + 1));
			}
		});

		return combo;
	}
	
	private static int indexOf(String[] values, String value) {
		int selected = -1;
		if (value != null) {
			for (int i = 0; i < values.length; i++) {
				if (value.equals(values[i])) {
					selected = i;
					break;
				}
			}
		}
		return selected;
	}
	
	private static Map<String, String>  checkNull(Map<String, String> items) {
		if (items == null)
			items = new HashMap<String, String>();
		else {
			for (Entry<String, String> value : items.entrySet()) {
				if (value.getKey() == null || value.getValue() == null)
					throw new RuntimeException("Null not allowed in key/value entries: " + items);
			}
		}
		return items;
	}
}
