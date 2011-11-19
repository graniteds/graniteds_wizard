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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.granite.wizard.bindings.Variable;

/**
 * @author Franck WOLFF
 */
public abstract class AbstractControl<T extends Control> {

	protected Variable variable;
	protected T control;

	public AbstractControl(Variable variable) {
		this.variable = variable;
	}
	
	public T getControl() {
		return control;
	}
	
	public T createControl(Composite parent, int widthHint) {
		control = internalCreateControl(parent, widthHint);
		return control;
	}
	
	protected abstract T internalCreateControl(Composite parent, int widthHint);
}
