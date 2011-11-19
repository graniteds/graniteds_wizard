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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.granite.wizard.bindings.Variable;
import org.granite.wizard.bindings.VariableChangeEvent;

/**
 * @author Franck WOLFF
 */
public class TextControl extends AbstractControl<Text> {

	public TextControl(Variable variable) {
		super(variable);
	}

	@Override
	protected Text internalCreateControl(Composite parent, int widthHint) {

		final Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		text.setText(variable.getValueAsString());
		text.setEnabled(!variable.isDisabled());
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = widthHint;
		text.setLayoutData(gridData);
		
		final ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				variable.setValue(text.getText());
				text.getParent().notifyListeners(VariableChangeEvent.ID, new VariableChangeEvent(text));
			}
		};
		
		text.addModifyListener(modifyListener);
		
		text.addListener(VariableChangeEvent.ID, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (((VariableChangeEvent)e).source == text)
					return;
				
				text.setEnabled(!variable.isDisabled());
				String value = variable.getValueAsString();
				if (!text.getText().equals(value)) {
					text.removeModifyListener(modifyListener);
					text.setText(value);
					text.addModifyListener(modifyListener);
				}
			}
		});
		
		return text;
	}
}
