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
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

public class HtmlReport extends HtmlPanelGroup 
implements UIReport {

	public static final String COMPONENT_TYPE =
		"net.sf.jasperreports.HtmlReport";
	
	// Report attributes
	
	private String dataSource = null;	
	private String path = null;
	private String subreportDir = null;
	private String format = null;
	
	// iframe attributes
	
	private boolean frameborder = false;
	private boolean frameborderSet = false;
	private String marginheight = null;
	private String marginwidth = null;
	private String height = null;
	private String width = null;
	
	public HtmlReport() {
		super();
		setRendererType(ReportRenderer.RENDERER_TYPE);
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
	
	public boolean getFrameborder() {
		if(frameborderSet) {
			return frameborder;
		}
		ValueExpression ve = getValueExpression("frameborder");
		if(ve != null) {
			try {
				return ((Boolean) ve.getValue(
						getFacesContext().getELContext())).booleanValue();
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return frameborder;
		}
	}
	
	public void setFrameborder(boolean frameborder) {
		this.frameborder = frameborder;
		this.frameborderSet = true;
	}
	
	public String getMarginheight() {
		if(marginheight != null) {
			return marginheight;
		}
		ValueExpression ve = getValueExpression("marginheight");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return marginheight;
		}
	}
	
	public void setMarginheight(String marginheight) {
		this.marginheight = marginheight;
	}
	
	public String getMarginwidth() {
		if(marginwidth != null) {
			return marginwidth;
		}
		ValueExpression ve = getValueExpression("marginwidth");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return marginwidth;
		}
	}
	
	public void setMarginwidth(String marginwidth) {
		this.marginwidth = marginwidth;
	}
	
	public String getHeight() {
		if(height != null) {
			return height;
		}
		ValueExpression ve = getValueExpression("height");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return height;
		}
	}
	
	public void setHeight(String height) {
		this.height = height;
	}
	
	public String getWidth() {
		if(width != null) {
			return width;
		}
		ValueExpression ve = getValueExpression("width");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return width;
		}
	}
	
	public void setWidth(String width) {
		this.width = width;
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
		frameborder = ((Boolean) values[5]).booleanValue();
		frameborderSet = ((Boolean) values[6]).booleanValue();
		marginheight = (String) values[7];
		marginwidth = (String) values[8];
		height = (String) values[9];
		width = (String) values[10];
	}
	
	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[11];
		values[0] = super.saveState(context);
		values[1] = dataSource;
		values[2] = path;
		values[3] = subreportDir;
		values[4] = format;
		values[5] = Boolean.valueOf(frameborder);
		values[6] = Boolean.valueOf(frameborderSet);
		values[7] = marginheight;
		values[8] = marginwidth;
		values[9] = height;
		values[10] = width;
		return values;
	}
	
}
