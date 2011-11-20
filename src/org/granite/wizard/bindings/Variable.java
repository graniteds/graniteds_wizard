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

import groovy.lang.Binding;
import groovy.lang.Closure;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.granite.wizard.bindings.controls.AbstractControl;
import org.granite.wizard.bindings.controls.Controls;

/**
 * @author Franck WOLFF
 */
public class Variable {
	
	private final Binding binding;
	private final String name;
	private final Map<String, Object> values;

	private AbstractControl<?> control;
	
	@SuppressWarnings("unchecked")
	public Variable(Binding binding, String name, String storedValue) {
		this.binding = binding;
		this.name = name;
		this.values = (Map<String, Object>)binding.getVariable(name);
		
		if (storedValue != null) {
			try {
				internalSet("value", convert(storedValue));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Binding getBinding() {
		return binding;
	}

	public String getName() {
		return name;
	}
	
	public String getLabel() {
		Object o = internalGet("label");
		return (o != null ? o.toString() : null);
	}
	
	public String getTooltip() {
		Object o = internalGet("tooltip");
		return (o != null ? o.toString() : null);
	}
	
	public ControlType getControlType() {
		return (ControlType)internalGet("controlType");
	}
	
	public AbstractControl<?> getControl() {
		return control;
	}
	
	public Control createControl(Composite parent, int widthHint) {
		return (control = Controls.createControl(parent, widthHint, this)).getControl();
	}
	
	public boolean isDisabled() {
		Object o = internalGet("disabled");
		return (o instanceof Boolean ? ((Boolean)o).booleanValue() : false);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getPossibleValues() {
		return (Map<String, String>)internalGet("possibleValues");
	}
	
	public Object getValue() {
		Object value = internalGet("value");
		return convert(value);
	}
	public void setValue(Object value) {
		internalSet("value", convert(value));
	}
	
	public String getValueAsString() {
		Object o = getValue();
		if (o instanceof File) try {
			return ((File)o).getCanonicalPath();
		}
		catch (IOException e) {
			// fallback...
		}
		return (o != null ? o.toString() : "");
	}
	public boolean getValueAsBoolean() {
		Object o = getValue();
		return (o instanceof Boolean ? ((Boolean)o).booleanValue() : false);
	}
	
	public Class<?> getType() {
		return (Class<?>)internalGet("type");
	}
	
	public String validate() {
		return validate(getValue());
	}
	
	public String validate(Object value) {
		if (control == null || control.getControl() == null || control.getControl().isDisposed())
			return null;
		
		String message = null;

		if (!isDisabled()) {
			try {
				Object o = internalGet("validate", new Object[]{value});
				if (o != null && !Boolean.parseBoolean(o.toString()))
					message = getErrorMessage();
			}
			catch (Exception e) {
				message = e.getMessage();
			}
		}
		
		return message;
	}

	public String getErrorMessage() {
		return (String)internalGet("errorMessage");
	}
	
	private void internalSet(String name, Object value) {
		Object previousValue = values.get(name);
		if (previousValue instanceof Closure) {
			if (previousValue instanceof MockClosure) {
				((MockClosure)previousValue).setValue(value);
				value = previousValue;
			}
			else
				value = new MockClosure((Closure<?>)previousValue, value);
		}
		values.put(name, value);
	}

	private Object internalGet(String name) {
		return internalGet(name, null);
	}
	private Object internalGet(String name, Object[] params) {
		Object value = values.get(name);
		if (value instanceof Closure) {
			if (params != null && params.length > 0)
				value = ((Closure<?>)value).call(params);
			else
				value = ((Closure<?>)value).call();
		}
		return value;
	}
	
	private Object convert(Object value) {
		Class<?> type = getType();
		if (!type.isInstance(value)) {
			if (type == File.class)
				value = new File(value != null ? value.toString() : "");
			else if (type == Boolean.class)
				value = Boolean.valueOf(value != null ? value.toString() : "false");
			else if (type == String.class)
				value = (value != null ? value : "");
			else
				throw new UnsupportedOperationException("Cannot convert " + value + " to " + type);
		}
		return value;
	}
}
