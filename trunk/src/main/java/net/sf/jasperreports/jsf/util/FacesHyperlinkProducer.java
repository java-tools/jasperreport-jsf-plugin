/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
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

/**
 * The Class FacesHyperlinkProducer.
 */
public class FacesHyperlinkProducer implements JRHyperlinkProducer {

    /** The Constant ACCEPT_REQUEST_HEADER. */
    private static final String ACCEPT_REQUEST_HEADER = "Accept";

    /** The context. */
    private final transient FacesContext context;

    /** The report. */
    private final transient UIComponent report;

    /**
     * Instantiates a new faces hyperlink producer.
     * 
     * @param context the context
     * @param report the report
     */
    public FacesHyperlinkProducer(final FacesContext context,
            final UIComponent report) {
        if ((context == null) || (report == null)) {
            throw new IllegalArgumentException();
        }
        this.context = context;
        this.report = report;
    }

    /*
     * (sin Javadoc)
     * @see
     * net.sf.jasperreports.engine.export.JRHyperlinkProducer#getHyperlink(net
     * .sf.jasperreports.engine.JRPrintHyperlink)
     */
    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.engine.export.JRHyperlinkProducer#getHyperlink(net
     * .sf.jasperreports.engine.JRPrintHyperlink)
     */
    public String getHyperlink(final JRPrintHyperlink link) {
    	final StringBuffer sb = new StringBuffer();
    	sb.append("<a");
    	sb.append(" href=\"").append(buildHref(link)).append("\"");
    	if(link.getHyperlinkAnchor() != null) {
    		sb.append(" name=\"").append(link.getHyperlinkAnchor()).append("\"");
    	}
    	
    	sb.append(" target=\"");
    	switch(link.getHyperlinkTarget()) {
    	
    	}
    	sb.append("\"");
    	
    	if(link.getHyperlinkTooltip() != null) {
    		sb.append(" title=\"").append(link.getHyperlinkTooltip()).append("\"");
    	}
    	
    	sb.append(">").append(link.getHyperlinkPage());
    	sb.append("</a>");
    	return sb.toString();
    }

    /**
     * Gets the response writer.
     * 
     * @param context the context
     * @param writer the writer
     * 
     * @return the response writer
     */
    private ResponseWriter getResponseWriter(final FacesContext context,
            final Writer writer) {
        final ResponseWriter current = context.getResponseWriter();
        if (current == null) {
            final UIViewRoot viewRoot = context.getViewRoot();
            final RenderKitFactory rkf = (RenderKitFactory) FactoryFinder
                    .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            final RenderKit rk = rkf.getRenderKit(context, viewRoot
                    .getRenderKitId());
            return rk.createResponseWriter(writer, context.getExternalContext()
                    .getRequestHeaderMap().get(ACCEPT_REQUEST_HEADER), context
                    .getExternalContext().getResponseCharacterEncoding());
        } else {
            return current.cloneWithWriter(writer);
        }
    }

    /**
     * Builds the href.
     * 
     * @param link the link
     * 
     * @return the string
     */
    private String buildHref(final JRPrintHyperlink link) {
        final StringBuffer buff = new StringBuffer();
        buff.append(link.getHyperlinkReference());
        if (link.getHyperlinkParameters().getParameters().size() > 0) {
            buff.append('?');
            for (int i = 0; i < link.getHyperlinkParameters().getParameters()
                    .size(); i++) {
                final JRHyperlinkParameter param = (JRHyperlinkParameter) link
                        .getHyperlinkParameters().getParameters().get(i);
                buff.append(param.getName());
                buff.append('=');
                buff.append(param.getValueExpression().getText());
            }
        }
        return context.getExternalContext().encodeResourceURL(buff.toString());
    }

}
