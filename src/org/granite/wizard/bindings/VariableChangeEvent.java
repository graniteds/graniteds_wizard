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

package org.granite.wizard.bindings;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Franck WOLFF
 */
public class VariableChangeEvent extends Event {
	
	public static int ID = 666666;
	
	public final Widget source;
	public final int index;
	
	public VariableChangeEvent() {
		this(null, 0);
	}
	
	public VariableChangeEvent(Widget source) {
		this(source, 0);
	}
	
	public VariableChangeEvent(Widget source, int index) {
		if (index < 0)
			throw new IllegalArgumentException("index must be posityive or null: " + index);
		this.source = source;
		this.index = index;
	}
}
