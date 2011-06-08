/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.engine.interop;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.List;

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
import net.sf.jasperreports.jsf.JRFacesException;

/**
 * The Class FacesHyperlinkProducer.
 */
public class FacesHyperlinkProducer implements JRHyperlinkProducer {
	
    /** The Constant ACCEPT_REQUEST_HEADER. */
    private static final String ACCEPT_REQUEST_HEADER = "Accept";
    
    /** The report. */
    private final UIComponent report;

    private ResponseWriter savedWriter = null;
    
    /**
     * Instantiates a new faces hyperlink producer.
     *
     * @param context the context
     * @param report the report
     */
    protected FacesHyperlinkProducer(final UIComponent report) {
        if (report == null) {
            throw new IllegalArgumentException();
        }
        this.report = report;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.engine.export.JRHyperlinkProducer#getHyperlink(net
     * .sf.jasperreports.engine.JRPrintHyperlink)
     */
    public String getHyperlink(final JRPrintHyperlink link) {
        final StringWriter sw = new StringWriter();
        final ResponseWriter writer = getResponseWriter(sw);

        try {
            writer.startElement("a", report);
            writer.writeAttribute("href", buildHref(link), null);

            String anchor;
            if ((anchor = link.getHyperlinkAnchor()) != null) {
                writer.writeAttribute("name", anchor, null);
            }

            String target = "_blank";
            switch (link.getHyperlinkTarget()) {
            default:
            }
            writer.writeAttribute("target", target, null);

            String tooltip;
            if ((tooltip = link.getHyperlinkTooltip()) != null) {
                writer.writeAttribute("title", tooltip, null);
            }

            writer.writeText(link.getHyperlinkPage(), report, null);

            writer.endElement("a");
        } catch (IOException e) {
            throw new JRFacesException(e);
        } finally {
            restoreWriter(getFacesContext());
        }

        return sw.toString();
    }

    /**
     * Gets the response writer.
     *
     * @param writer the writer
     *
     * @return the response writer
     */
    private ResponseWriter getResponseWriter(final Writer writer) {
        FacesContext context = getFacesContext();
        ResponseWriter current = context.getResponseWriter();
        if (current == null) {
            final UIViewRoot viewRoot = context.getViewRoot();
            final RenderKitFactory rkf = (RenderKitFactory)
                    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            final RenderKit rk = rkf.getRenderKit(
                    context, viewRoot.getRenderKitId());
            current = rk.createResponseWriter(writer,
                    context.getExternalContext().getRequestHeaderMap()
                        .get(ACCEPT_REQUEST_HEADER),
                    context.getExternalContext()
                        .getResponseCharacterEncoding());
        } else {
            savedWriter = current;
            current = current.cloneWithWriter(writer);
        }
        context.setResponseWriter(current);
        return current;
    }

    private void restoreWriter(FacesContext context) {
        context.setResponseWriter(savedWriter);
        savedWriter = null;
    }
    
    /**
     * Builds the href.
     *
     * @param link the link
     *
     * @return the string
     */
    private String buildHref(final JRPrintHyperlink link) {
        final StringBuilder buff = new StringBuilder();
        buff.append(link.getHyperlinkReference());

        String anchor;
        if ((anchor = link.getHyperlinkAnchor()) != null) {
        	buff.append("#");
        	buff.append(anchor);
        }
        
        List<?> params = null;
        if (link.getHyperlinkParameters() != null) {
            params = link.getHyperlinkParameters().getParameters();
        }
        if (params != null && params.size() > 0) {
            buff.append('?');
            for (int i = 0; i < params.size(); i++) {
                final JRHyperlinkParameter param =
                        (JRHyperlinkParameter) params.get(i);
                if (i > 0) {
                    buff.append("&");
                }
                buff.append(param.getName());
                buff.append('=');
                buff.append(param.getValueExpression().getText());
            }
        }

        return getFacesContext().getExternalContext()
                .encodeResourceURL(buff.toString());
    }

    private FacesContext getFacesContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new IllegalStateException("No current FacesContext!");
        }
        return context;
    }

}
