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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.granite.wizard.bindings.ValidationException;
import org.granite.wizard.bindings.Variable;

/**
 * @author Franck WOLFF
 */
public abstract class AbstractControl<T extends Control> {
	
	protected static final Color ERROR_COLOR = new Color(
		PlatformUI.getWorkbench().getDisplay(),
		new RGB(0xff, 0x80, 0x80)
	);

	protected Variable variable;
	protected T control;
	protected Color background;

	public AbstractControl(Variable variable) {
		this.variable = variable;
	}
	
	public T getControl() {
		return control;
	}
	
	public T createControl(Composite parent, int widthHint) {
		control = internalCreateControl(parent, widthHint);
		background = control.getBackground();
		return control;
	}
	
	protected abstract T internalCreateControl(Composite parent, int widthHint);
	
	public abstract void displayError(ValidationException e);
	
	public abstract void resetError();
}
