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

import net.sf.jasperreports.jsf.component.html.HtmlReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

public class ReportTag extends AbstractReportTag {
	
	private ValueExpression frameborder = null;
	private ValueExpression marginheight = null;
	private ValueExpression marginwidth = null;
	private ValueExpression height = null;
	private ValueExpression width = null;
	
	/**
	 * @param frameborder el frameborder a establecer
	 */
	public void setFrameborder(ValueExpression frameborder) {
		this.frameborder = frameborder;
	}

	/**
	 * @param height el height a establecer
	 */
	public void setHeight(ValueExpression height) {
		this.height = height;
	}

	/**
	 * @param marginheight el marginheight a establecer
	 */
	public void setMarginheight(ValueExpression marginheight) {
		this.marginheight = marginheight;
	}

	/**
	 * @param marginwidth el marginwidth a establecer
	 */
	public void setMarginwidth(ValueExpression marginwidth) {
		this.marginwidth = marginwidth;
	}

	/**
	 * @param width el width a establecer
	 */
	public void setWidth(ValueExpression width) {
		this.width = width;
	}
	
	// TagSupport
	
	public void release() {
		super.release();
		frameborder = null;
		marginheight = null;
		marginwidth = null;
		height = null;
		width = null;
	}
	
	// UIComponentELTag

	@Override
	public String getComponentType() {
		return HtmlReport.COMPONENT_TYPE;
	}
	
	@Override
	public String getRendererType() {
		return ReportRenderer.RENDERER_TYPE;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		HtmlReport report = (HtmlReport) component;
		
		if(frameborder != null) {
			if(frameborder.isLiteralText()) {
				report.setFrameborder(Boolean.parseBoolean(
						frameborder.getExpressionString()));
			} else {
				report.setValueExpression("frameborder", frameborder);
			}
		}
		
		if(marginheight != null) {
			if(marginheight.isLiteralText()) {
				report.setMarginheight(marginheight.getExpressionString());
			} else {
				report.setValueExpression("marginheight", marginheight);
			}
		}
		
		if(marginwidth != null) {
			if(marginwidth.isLiteralText()) {
				report.setMarginwidth(marginwidth.getExpressionString());
			} else {
				report.setValueExpression("marginwidth", marginwidth);
			}
		}
		
		if(height != null) {
			if(height.isLiteralText()) {
				report.setHeight(height.getExpressionString());
			} else {
				report.setValueExpression("height", height);
			}
		}
		
		if(width != null) {
			if(width.isLiteralText()) {
				report.setWidth(width.getExpressionString());
			} else {
				report.setValueExpression("width", width);
			}
		}
	}
	
}
