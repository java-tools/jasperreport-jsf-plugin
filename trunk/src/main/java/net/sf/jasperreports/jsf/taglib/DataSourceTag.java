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
package net.sf.jasperreports.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

import net.sf.jasperreports.jsf.component.UIDataSource;

public class DataSourceTag extends UIComponentELTag {

	private ValueExpression driverClass = null;
	private ValueExpression query = null;
	private ValueExpression type = null;
	private ValueExpression value = null;
	
	/**
	 * @param driverClass el driverClass a establecer
	 */
	public void setDriverClass(ValueExpression driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * @param query el query a establecer
	 */
	public void setQuery(ValueExpression query) {
		this.query = query;
	}

	/**
	 * @param type el type a establecer
	 */
	public void setType(ValueExpression type) {
		this.type = type;
	}

	/**
	 * @param value el value a establecer
	 */
	public void setValue(ValueExpression value) {
		this.value = value;
	}

	// TagSupport
	
	@Override
	public void release() {
		super.release();
		driverClass = null;
		query = null;
		type = null;
		value = null;
	}
	
	// UIComponentELTag
	
	public String getComponentType() {
		return UIDataSource.COMPONENT_TYPE;
	}
	
	public String getRendererType() {
		return null;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		UIDataSource dataSource = (UIDataSource) component;
		
		if(driverClass != null) {
			if(driverClass.isLiteralText()) {
				dataSource.setDriverClass(driverClass.getExpressionString());
			} else {
				dataSource.setValueExpression("driverClass", driverClass);
			}
		}
		
		if(query != null) {
			if(query.isLiteralText()) {
				dataSource.setQuery(query.getExpressionString());
			} else {
				dataSource.setValueExpression("query", query);
			}
		}
		
		if(type != null) {
			if(type.isLiteralText()) {
				dataSource.setType(type.getExpressionString());
			} else {
				dataSource.setValueExpression("type", type);
			}
		}
		
		if(value != null) {
			if(value.isLiteralText()) {
				dataSource.setValue(value.getExpressionString());
			} else {
				dataSource.setValueExpression("value", value);
			}
		}
	}
	
}
