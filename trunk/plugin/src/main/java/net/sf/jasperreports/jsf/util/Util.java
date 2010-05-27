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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.component.UIReport;

/**
 * The Class Util.
 */
public final class Util {

    private static final Logger logger = Logger.getLogger(
            Util.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");
    
    /** The Constant INVOCATION_PATH. */
    private static final String INVOCATION_PATH =
            "net.sf.jasperreports.jsf.INVOCATION_PATH";

    /**
     * Gets the class loader.
     *
     * @param fallback
     *            the fallback
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
     * Gets the faces mapping.
     *
     * @param context
     *            the context
     *
     * @return the faces mapping
     */
    public static String getInvocationPath(final FacesContext context) {
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
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "JRJSF_0018", mapping);
            }
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        } else {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "JRJSF_0019");
            }
        }

        return mapping;
    }

    /**
     * <p>
     * Returns true if the provided <code>url-mapping</code> is a prefix path
     * mapping (starts with <code>/</code>).
     * </p>
     *
     * @param mapping
     *            a <code>url-pattern</code>
     *
     * @return true if the mapping starts with <code>/</code>
     */
    public static boolean isPrefixMapped(final String mapping) {
        if (mapping == null || mapping.length() == 0) {
            throw new IllegalArgumentException(
                    "'mapping' can't be null or empty");
        }
        return mapping.charAt(0) == '/';
    }

    public static String resolveDataSourceId(FacesContext context, UIReport component,
            String dataSourceId) {
        return null;
    }

    /**
     * <p>
     * Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.
     * </p>
     *
     * @param servletPath
     *            the servlet path of the request
     * @param pathInfo
     *            the path info of the request
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

    private Util() { }

}
