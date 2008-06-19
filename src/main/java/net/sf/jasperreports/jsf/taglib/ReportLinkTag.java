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

import net.sf.jasperreports.jsf.component.html.HtmlReportLink;
import net.sf.jasperreports.jsf.renderkit.LinkRenderer;

public class ReportLinkTag extends AbstractReportTag {
	
	private ValueExpression target = null;
	
	public void setTarget(ValueExpression target) {
		this.target = target;
	}
	
	// TagSupport
	
	public void release() {
		super.release();
		target = null;
	}
	
	// UIComponentELTag
	
	@Override
	public String getComponentType() {
		return HtmlReportLink.COMPONENT_TYPE;
	}
	
	@Override
	public String getRendererType() {
		return LinkRenderer.RENDERER_TYPE;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		HtmlReportLink report = (HtmlReportLink) component;
		
		if(target != null) {
			if(target.isLiteralText()) {
				report.setTarget(target.getExpressionString());
			} else {
				report.setValueExpression("target", target);
			}
		}
	}
	
}
