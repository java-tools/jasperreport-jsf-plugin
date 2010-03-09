/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
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
import java.net.URLEncoder;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

public abstract class ExternalContextHelper {

    private static final String INSTANCE_KEY =
            ExternalContextHelper.class.getName();

    /** The Constant PORTLET_CLASS. */
    private static final String PORTLET_CLASS = "javax.portlet.Portlet";

    /** The Constant PORTLET_RESOURCEURL_CLASS. */
    private static final String PORTLET_RESOURCEURL_CLASS = "javax.portlet.ResourceURL";

    public static ExternalContextHelper getInstance(final ExternalContext context) {
        ExternalContextHelper instance = (ExternalContextHelper)
                context.getApplicationMap().get(INSTANCE_KEY);
        if (instance == null) {
            if (isServletContext(context)) {
                instance = new ServletContextHelper();
            } else if (isPortletAvailable()) {
                instance = new PortletContextHelper();
            } else {
                throw new IllegalArgumentException(
                        "Unrecognized application context");
            }
            context.getApplicationMap().put(INSTANCE_KEY, instance);
        }
        return instance;
    }

    public static boolean isPortletAvailable() {
        boolean portletAvailable = false;
        try {
            Class.forName(PORTLET_CLASS);
            portletAvailable = true;
        } catch (final ClassNotFoundException e) {
            portletAvailable = false;
        } catch (final NoClassDefFoundError e) {
            portletAvailable = false;
        }
        return portletAvailable;
    }

    public static String getPortletVersion() {
        final boolean portletAvailable = isPortletAvailable();

        String portletVersion = null;
        try {
            Class.forName(PORTLET_RESOURCEURL_CLASS);
            portletVersion = "2.0";
        } catch (final ClassNotFoundException e) {
            portletVersion = portletAvailable ? "1.0" : null;
        } catch (final NoClassDefFoundError e) {
            portletVersion = portletAvailable ? "1.0" : null;
        }
        return portletVersion;
    }

    public static boolean isServletContext(final ExternalContext context) {
        final Object ctx = context.getContext();
        return (ctx instanceof ServletContext);
    }

    protected ExternalContextHelper() { }

    /**
     * Gets the request uri.
     *
     * @param context
     *            the context
     *
     * @return the request uri
     */
    public abstract String getRequestURI(final ExternalContext context);

    public abstract String getRequestServerName(final ExternalContext context);

    public abstract String getResourceRealPath(
            final ExternalContext context, String name);

    public String getViewId(final ExternalContext context) {
        String pathInfo = context.getRequestPathInfo();
        String servletPath = context.getRequestServletPath();
        if (pathInfo == null) {
            String suffix = context.getInitParameter(
                    ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            if (suffix == null || suffix.length() == 0) {
                suffix = ViewHandler.DEFAULT_SUFFIX;
            }
            return servletPath.substring(0, servletPath.lastIndexOf('.')) + suffix;
        } else {
            return pathInfo;
        }
    }

    public abstract void writeHeaders(ExternalContext context,
            ReportRenderer renderer, UIReport report) throws IOException;

    /**
     * Write response.
     *
     * @param context
     *            the context
     * @param contentType
     *            the content type
     * @param data
     *            the data
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public abstract void writeResponse(final ExternalContext context,
            final String contentType, final byte[] data) throws IOException;

    protected String encodeContentDisposition(final ReportRenderer renderer,
            final UIReport report, final String enc) throws IOException {
        final StringBuffer disposition = new StringBuffer();
        if (report.getName() != null) {
            disposition.append(renderer.getContentDisposition());
            disposition.append("; filename=");
            disposition.append(URLEncoder.encode(report.getName(), enc));
        }
        return disposition.toString();
    }
}
