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
package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import net.sf.jasperreports.jsf.ReportPhaseListener;
import net.sf.jasperreports.jsf.util.Util;

public abstract class AbstractReportRenderer extends Renderer {

	private static final String[] PASSTHRU_ATTRS = {
		"dir", "lang", "title", "style", "datafld", "datasrc", "dataformatas", 
		"ondblclick", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", 
		"accesskey", "tabindex", "height", "width"
	};
	
	@Override
	public final boolean getRendersChildren() {
		return true;
	}
	
	protected final String buildReportURI(FacesContext context, UIComponent report) {
		StringBuffer reportURI = new StringBuffer(ReportPhaseListener.BASE_URI);
		
		String mapping = Util.getFacesMapping(context);
		if(Util.isPrefixMapped(mapping)) {
			reportURI.insert(0, mapping);
		} else  {
			reportURI.append(mapping);
		}
		
		reportURI.append("?").append(ReportPhaseListener.PARAM_CLIENTID);
		reportURI.append("=").append(report.getClientId(context));
		
		return context.getExternalContext().encodeResourceURL(reportURI.toString());
	}
	
	protected final void renderIdAttribute(FacesContext context, UIComponent report)
	throws IOException {
		String id = report.getId();
		if((id != null) && (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX))) {
			ResponseWriter writer = context.getResponseWriter();
			writer.writeAttribute("id", report.getClientId(context), "id");
		}
	}
	
	protected final void renderAttributes(ResponseWriter writer, UIComponent report)
	throws IOException {
		String styleClass = (String) report.getAttributes().get("styleClass");
		if(styleClass != null) {
			writer.writeAttribute("class", styleClass, null);
		}
		
		for(String attrName : PASSTHRU_ATTRS) {
			Object value = report.getAttributes().get(attrName);
			if(value != null) {
				writer.writeAttribute(attrName, value, null);
			}
		}
	}
	
}
