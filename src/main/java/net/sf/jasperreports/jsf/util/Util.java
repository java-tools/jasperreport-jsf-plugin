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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletContext;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;

// TODO: Auto-generated Javadoc
/**
 * The Class Util.
 */
public final class Util {

    /** The Constant INVOCATION_PATH. */
    private static final String INVOCATION_PATH = "net.sf.jasperreports.jsf.INVOCATION_PATH";

    /** The Constant PORTLET_CLASS. */
    private static final String PORTLET_CLASS = "javax.portlet.Portlet";

    /** The Constant PORTLET_RESOURCEURL_CLASS. */
    private static final String PORTLET_RESOURCEURL_CLASS = "javax.portlet.ResourceURL";

    /** The Constant PORTLET_AVAILABLE. */
    public static final boolean PORTLET_AVAILABLE;

    /** The Constant PORTLET_VERSION. */
    public static final String PORTLET_VERSION;

    /** The logger. */
    private static final Logger logger = Logger.getLogger(
    		Util.class.getPackage().getName(), 
    		"net.sf.jasperreports.jsf.LogMessages");

    static {
        boolean portletAvailable = false;
        try {
            Class.forName(PORTLET_CLASS);
            portletAvailable = true;
        } catch (final ClassNotFoundException e) {
            portletAvailable = false;
        }

        String portletVersion = null;
        try {
            Class.forName(PORTLET_RESOURCEURL_CLASS);
            portletVersion = "2.0";
        } catch (final ClassNotFoundException e) {
            portletVersion = portletAvailable ? "1.0" : null;
        }

        PORTLET_AVAILABLE = portletAvailable;
        PORTLET_VERSION = portletVersion;
    }

    /**
     * Gets the class loader.
     * 
     * @param fallback the fallback
     * 
     * @return the class loader
     */
    public static ClassLoader getClassLoader(final Object fallback) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            if (fallback == null) { // NOPMD by antonio.alonso on 20/10/08 15:39
                loader = Util.class.getClassLoader(); // NOPMD by antonio.alonso
                                                      // on 20/10/08 15:40
            } else {
                loader = fallback.getClass().getClassLoader(); // NOPMD by
                                                               // antonio.alonso
                                                               // on 20/10/08
                                                               // 15:40
            }
        }
        return loader;
    }

    /**
     * Gets the data source component.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the data source component
     */
    public static UIDataSource getDataSourceComponent(
            final FacesContext context, final UIReport report) {
        UIDataSource dataSource = null;
        for (final UIComponent child : ((UIComponent) report).getChildren()) {
            if (child instanceof UIDataSource) {
                dataSource = (UIDataSource) child;
                break;
            }
        }
        if ((dataSource == null) && (report.getDataSource() != null)) {
            final String dataSourceId = report.getDataSource();
            dataSource = (UIDataSource) ((UIComponent) report)
                    .findComponent(dataSourceId);
            if (dataSource == null) {
                UIComponent container = (UIComponent) report;
                while (!(container instanceof NamingContainer)) {
                    container = container.getParent();
                }
                dataSource = (UIDataSource) container
                        .findComponent(dataSourceId);
            }
        }
        if ((dataSource != null) && logger.isLoggable(Level.FINE)) {
            final String dsClientId = dataSource.getClientId(context);
            logger.log(Level.FINE, "JRJSF_0009", dsClientId);
        }
        return dataSource;
    }

    /**
     * Gets the faces mapping.
     * 
     * @param context the context
     * 
     * @return the faces mapping
     */
    public static String getFacesMapping(final FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        }

        // Check for a previously stored mapping
        final ExternalContext extContext = context.getExternalContext();
        String mapping = (String) extContext.getRequestMap().get(
                INVOCATION_PATH);

        if (mapping == null) {

            extContext.getRequest();
            String servletPath = null;
            String pathInfo = null;

            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values. if either is non-null, use this
            // information to generate determine the mapping.

            servletPath = extContext.getRequestServletPath();
            pathInfo = extContext.getRequestPathInfo();

            mapping = getMappingForRequest(servletPath, pathInfo);
        }

        // if the FacesServlet is mapped to /* throw an
        // Exception in order to prevent an endless
        // RequestDispatcher loop
        if ("/*".equals(mapping)) {
            throw new FacesException("Illegal faces mapping: " + mapping);
        }

        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }

        return mapping;
    }

    /**
     * Checks if is portlet context.
     * 
     * @param facesContext the faces context
     * 
     * @return true, if is portlet context
     */
    public static boolean isPortletContext(final FacesContext facesContext) {
        if (!PORTLET_AVAILABLE) {
            return false;
        }

        final Object context = facesContext.getExternalContext().getContext();
        return context instanceof PortletContext;
    }

    /**
     * Gets the request uri.
     * 
     * @param context the context
     * 
     * @return the request uri
     */
    public static String getRequestURI(final FacesContext context) {
        if (isPortletContext(context)) {
            if ("2.0".equals(PORTLET_VERSION)) {
                final ResourceRequest request = (ResourceRequest) context
                        .getExternalContext().getRequest();
                return request.getResourceID();
            } else {
                return null;
            }
        } else {
            final HttpServletRequest request = (HttpServletRequest) context
                    .getExternalContext().getRequest();
            return request.getRequestURI();
        }
    }

    /**
     * <p>
     * Returns true if the provided <code>url-mapping</code> is a prefix path
     * mapping (starts with <code>/</code>).
     * </p>
     * 
     * @param mapping a <code>url-pattern</code>
     * 
     * @return true if the mapping starts with <code>/</code>
     */
    public static boolean isPrefixMapped(final String mapping) {
        return mapping.charAt(0) == '/';
    }

    /**
     * Load service map.
     * 
     * @param resource the resource
     * 
     * @return the map< string, class< t>>
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Class<T>> loadServiceMap(final String resource) {
        final ClassLoader loader = Util.getClassLoader(null);
        Enumeration<URL> resources;
        try {
            resources = loader.getResources(resource);
        } catch (final IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        final Map<String, Class<T>> serviceMap = new LinkedHashMap<String, Class<T>>();
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            BufferedReader reader = null;
            try {
                String line;
                reader = new BufferedReader(new InputStreamReader(url
                        .openStream()));
                while (null != (line = reader.readLine())) {
                    Class<T> serviceClass;
                    final String[] record = line.split(":");
                    try {
                        serviceClass = (Class<T>) loader.loadClass(record[1]);
                    } catch (final ClassNotFoundException e) {
                    	LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0011");
                    	logRecord.setParameters(new Object[]{ record[1], record[0] });
                    	logRecord.setThrown(e);
                        logger.log(logRecord);
                        continue;
                    }

                    serviceMap.put(
                            ("null".equals(record[0]) ? null : record[0]),
                            serviceClass);
                }
            } catch (final IOException e) {
            	LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0012");
            	logRecord.setParameters(new Object[]{ url });
            	logRecord.setThrown(e);
            	logger.log(logRecord);
                continue;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        // ignore
                    }
                }
            }
        }
        return serviceMap;
    }

    /**
     * Write response.
     * 
     * @param context the context
     * @param contentType the content type
     * @param data the data
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeResponse(final FacesContext context,
            final String contentType, final byte[] data) throws IOException {
        if (isPortletContext(context)) {
            if ("2.0".equals(PORTLET_VERSION)) {
                final ResourceResponse response = (ResourceResponse) context
                        .getExternalContext().getResponse();
                response.setContentType(contentType);
                response.setContentLength(data.length);
                response.getPortletOutputStream().write(data);

                response.setProperty("Cache-Type", "no-cache");
                response.setProperty("Expires", "0");
            } else {
                throw new IllegalStateException(
                        "Only Resource Request/Response state is allowed");
            }
        } else {
            final HttpServletResponse response = (HttpServletResponse) context
                    .getExternalContext().getResponse();
            response.setContentType(contentType);
            response.setContentLength(data.length);
            response.getOutputStream().write(data);

            response.setHeader("Cache-Type", "no-cache");
            response.setHeader("Expires", "0");
        }
    }

    /**
     * <p>
     * Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.
     * </p>
     * 
     * @param servletPath the servlet path of the request
     * @param pathInfo the path info of the request
     * 
     * @return the appropriate mapping based on the current request
     * 
     * @see HttpServletRequest#getServletPath()
     */
    private static String getMappingForRequest(final String servletPath,
            final String pathInfo) {

        if (servletPath == null) {
            return null;
        }
        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
            return "/*";
        }

        // presence of path info means we were invoked
        // using a prefix path mapping
        if (pathInfo != null) {
            return servletPath;
        } else if (servletPath.indexOf('.') < 0) {
            // if pathInfo is null and no '.' is present, assume the
            // FacesServlet was invoked using prefix path but without
            // any pathInfo - i.e. GET /contextroot/faces or
            // GET /contextroot/faces/
            return servletPath;
        } else {
            // Servlet invoked using extension mapping
            return servletPath.substring(servletPath.lastIndexOf('.'));
        }
    }

    /**
     * Instantiates a new util.
     */
    private Util() {}

}
