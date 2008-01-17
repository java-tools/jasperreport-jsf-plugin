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
package net.sf.jasperreports.jsf.component.html;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportLinkRenderer;

public class HtmlReportLink extends HtmlOutputLink 
implements UIReport {

	public static final String COMPONENT_TYPE =
		"net.sf.jasperreports.HtmlReportLink";
	
	private String dataSource = null;	
	private String path = null;
	private String subreportDir = null;
	private String format = null;
	
	public HtmlReportLink() {
		super();
		setRendererType(ReportLinkRenderer.RENDERER_TYPE);
	}
	
	public String getDataSource() {
		if(dataSource != null) {
			return dataSource;
		}
		ValueExpression ve = getValueExpression("dataSource");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return dataSource;
		}
	}
	
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getPath() {
		if(path != null) {
			return path;
		}
		ValueExpression ve = getValueExpression("path");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return path;
		}
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getSubreportDir() {
		if(subreportDir != null) {
			return subreportDir;
		}
		ValueExpression ve = getValueExpression("subreportDir");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return subreportDir;
		}
	}
	
	public void setSubreportDir(String subreportDir) {
		this.subreportDir = subreportDir;
	}
	
	public String getFormat() {
		if(format != null) {
			return format;
		}
		ValueExpression ve = getValueExpression("format");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return format;
		}
	}
	
	public void setFormat(String type) {
		this.format = type;
	}
	
	@Override
	public String getFamily() {
		return UIReport.COMPONENT_FAMILY;
	}
	
	// State saving/restoring methods
	
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		dataSource = (String) values[1];
		path = (String) values[2];
		subreportDir = (String) values[3];
		format = (String) values[4];
	}
	
	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = dataSource;
		values[2] = path;
		values[3] = subreportDir;
		values[4] = format;
		return values;
	}
	
}
