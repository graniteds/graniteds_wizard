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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.granite.wizard.bindings.Variable;

/**
 * @author Franck WOLFF
 */
public class Controls {
	
	public static AbstractControl<?> createControl(Composite parent, Label label, int widthHint, Variable variable) {
		
		AbstractControl<?> control = null;
		
		switch (variable.getControlType()) {
		case TEXT:
			control = new TextControl(variable); break;
		case CHECKBOX:
			control = new CheckboxControl(variable); break;
		case COMBO:
			control = new ComboControl(variable); break;
		case DIRECTORY:
			control = new DirectoryControl(variable); break;
		default:
			control = new UnknownControl(variable); break;
		}
		
		control.createControl(parent, label, widthHint);
		
		return control;
	}
	
	public static class UnknownControl extends AbstractControl<Label> {

		public UnknownControl(Variable variable) {
			super(variable);
		}

		@Override
		protected Label internalCreateControl(Composite parent, int widthHint) {
			Label label = new Label(parent, SWT.NONE);
			label.setText("Unknown control type: " + variable.getControlType().toString());
			return label;
		}
	}
}
