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
import java.io.Writer;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;

public class FacesHyperlinkProducer implements JRHyperlinkProducer {

	private static final String ACCEPT_REQUEST_HEADER = "Accept";

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
			ResponseWriter writer = getResponseWriter(context, sw);
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
	
	private ResponseWriter getResponseWriter(FacesContext context, Writer writer) {
		ResponseWriter current = context.getResponseWriter();
		if(current == null) {
			UIViewRoot viewRoot = context.getViewRoot();
			RenderKitFactory rkf = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
			RenderKit rk = rkf.getRenderKit(context, viewRoot.getRenderKitId());
			return rk.createResponseWriter(writer, 
					context.getExternalContext().getRequestHeaderMap().get(ACCEPT_REQUEST_HEADER),
					context.getExternalContext().getResponseCharacterEncoding());
		} else {
			return current.cloneWithWriter(writer);
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
