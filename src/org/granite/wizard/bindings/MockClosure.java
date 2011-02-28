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

import groovy.lang.Closure;

/**
 * @author Franck WOLFF
 */
public class MockClosure extends Closure {
	
	private static final long serialVersionUID = 1L;

	private final Closure original;
	private Object value;
	
	public MockClosure(Closure original, Object value) {
		super(null);
		
		this.original = original;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public Object call() {
		return value;
	}

	@Override
	public Object call(Object arguments) {
		return value;
	}

	@Override
	public Object call(Object[] arg0) {
		return value;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return original.getParameterTypes();
	}
}
