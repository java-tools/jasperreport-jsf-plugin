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
package net.sf.jasperreports.jsf.util;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;

public class FacesHyperlinkProducer implements JRHyperlinkProducer {

	private FacesContext context;
	private UIComponent report;
	
	public FacesHyperlinkProducer(FacesContext context, UIComponent report) {
		if(context == null || report == null) {
			throw new NullPointerException();
		}
		this.context = context;
		this.report = report;
	}
	
	/* (sin Javadoc)
	 * @see net.sf.jasperreports.engine.export.JRHyperlinkProducer#getHyperlink(net.sf.jasperreports.engine.JRPrintHyperlink)
	 */
	public String getHyperlink(JRPrintHyperlink link) {
		StringWriter sw = new StringWriter();
		try {
			ResponseWriter writer = context.getResponseWriter().cloneWithWriter(sw);
			writer.startElement("a", report);
			writer.writeAttribute("href", buildHref(link), null);
			if(null != link.getHyperlinkAnchor()) {
				writer.writeAttribute("name", link.getHyperlinkAnchor(), null);
			}
			/*if(null != link.getHyperlinkTarget()) {
				
			}*/
			if(null != link.getHyperlinkTooltip()) {
				writer.writeAttribute("title", link.getHyperlinkTooltip(), null);
			}
			writer.writeText(link.getHyperlinkPage(), null);
			writer.endElement("a");
			return sw.toString();
		} catch(IOException e) {
			return null;
		} finally {
			try {
				sw.close();
			} catch(IOException e) { }
		}
	}

	private String buildHref(JRPrintHyperlink link) {
		StringBuffer buff = new StringBuffer();
		buff.append(link.getHyperlinkReference());
		if(link.getHyperlinkParameters().getParameters().size() > 0) {
			buff.append("?");
			for(int i = 0;i < link.getHyperlinkParameters().getParameters().size();i++) {
				JRHyperlinkParameter param = (JRHyperlinkParameter) link
						.getHyperlinkParameters().getParameters().get(i);
				buff.append(param.getName());
				buff.append("=");
				buff.append(param.getValueExpression().getText());
			}
		}
		return context.getExternalContext().encodeResourceURL(buff.toString());
	}
	
}
