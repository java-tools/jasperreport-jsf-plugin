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

import net.sf.jasperreports.jsf.component.UIReport;

public abstract class AbstractReportTag extends UIComponentELTag {

	private ValueExpression dataSource = null;
	private ValueExpression path = null;
	private ValueExpression subreportDir = null;
	private ValueExpression format = null;
	
	public void setDataSource(ValueExpression dataSource) {
		this.dataSource = dataSource;
	}

	public void setPath(ValueExpression report) {
		this.path = report;
	}
	
	public void setSubreportDir(ValueExpression subreportDir) {
		this.subreportDir = subreportDir;
	}

	public void setFormat(ValueExpression type) {
		this.format = type;
	}

	// TagSupport
	
	@Override
	public void release() {
		super.release();
		dataSource = null;
		path = null;
		subreportDir = null;
		format = null;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		
		UIReport jreport = (UIReport) component;
		if(dataSource != null) {
			if(dataSource.isLiteralText()) {
				jreport.setDataSource(dataSource.getExpressionString());
			} else {
				component.setValueExpression("dataSource", dataSource);
			}
		}
				
		if(path != null) {
			if(path.isLiteralText()) {
				jreport.setPath(path.getExpressionString());
			} else {
				component.setValueExpression("report", path);
			}
		}
				
		if(subreportDir != null) {
			if(subreportDir.isLiteralText()) {
				jreport.setSubreportDir(subreportDir.getExpressionString());
			} else {
				component.setValueExpression("subreportDir", subreportDir);
			}
		}
		
		if(format != null) {
			if(format.isLiteralText()) {
				jreport.setFormat(format.getExpressionString());
			} else {
				component.setValueExpression("format", format);
			}
		}
	}
	
}
