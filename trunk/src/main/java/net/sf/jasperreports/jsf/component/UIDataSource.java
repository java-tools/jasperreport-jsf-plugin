/* JaspertReports JSF Plugin
 * Copyright (C) 2008 A. Alonso Dominguez
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * A. Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.component;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class UIDataSource extends UIComponentBase {

	public static final String COMPONENT_FAMILY =
		"net.sf.jasperreports.DataSource";
	public static final String COMPONENT_TYPE =
		"net.sf.jasperreports.DataSource";
	
	public static final String BEAN = "bean";
	public static final String CSV = "csv";
	public static final String DEFAULT = "default";
	public static final String EMPTY = "empty";
	public static final String JNDI = "jndi";
	public static final String MAP = "map";
	public static final String RESULT_SET = "resultSet";
	public static final String XML = "xml";
	
	// Fields
	
	private String driverClass = null;
	
	private String query = null;
	
	private String type = DEFAULT;
	private boolean typeSet = false;
	
	private Object value = null;

	public UIDataSource() {
		super();
		setRendererType(null);
	}
	
	// Properties
	
	public String getDriverClass() {
		if(driverClass != null) {
			return driverClass;
		}
		ValueExpression ve = getValueExpression("driverClass");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return driverClass;
		}
	}
	
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	
	public String getQuery() {
		if(query != null) {
			return query;
		}
		ValueExpression ve = getValueExpression("query");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return query;
		}
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getType() {
		if(typeSet) {
			return type;
		}
		ValueExpression ve = getValueExpression("type");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return type;
		}
	}

	public void setType(String type) {
		this.type = type;
		this.typeSet = true;
	}
	
	public Object getValue() {
		if(value != null) {
			return value;
		}
		ValueExpression ve = getValueExpression("value");
		if(ve != null) {
			try {
				return ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return value;
		}
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	// UIComponent
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		driverClass = (String) values[1];
		query = (String) values[2];
		type = (String) values[3];
		typeSet = ((Boolean) values[4]).booleanValue();
		value = values[5];
	}
	
	public Object saveState(FacesContext context) {
		Object[] values = new Object[6];
		values[0] = super.saveState(context);
		values[1] = driverClass;
		values[2] = query;
		values[3] = type;
		values[4] = Boolean.valueOf(typeSet);
		values[5] = value;
		return values;
	}
	
}
