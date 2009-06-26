/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
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

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * The Class Util.
 */
public abstract class Util {

	/** The Constant PORTLET_CLASS. */
	protected static final String PORTLET_CLASS = "javax.portlet.Portlet";

	/** The Constant PORTLET_RESOURCEURL_CLASS. */
	protected static final String PORTLET_RESOURCEURL_CLASS = "javax.portlet.ResourceURL";

	/** The Constant INVOCATION_PATH. */
	private static final String INVOCATION_PATH = "net.sf.jasperreports.jsf.INVOCATION_PATH";
	
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
	
	public static Util getInstance(FacesContext context) {
		Util instance;
		if(isServletContext(context)) {
			instance = new ServletUtil();
		} else if(isPortletAvailable()) {
			instance = new PortletUtil();
		} else {
			throw new IllegalArgumentException("Unrecognized application context");
		}
		return instance;
	}
	
	protected static boolean isPortletAvailable() {
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
	
	protected static String getPortletVersion() {
		boolean portletAvailable = isPortletAvailable();
		
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
	
	private static boolean isServletContext(FacesContext context) {
		Object ctx = context.getExternalContext().getContext();
		return (ctx instanceof ServletContext);
	}
	
	/**
	 * Instantiates a new util.
	 */
	protected Util() {
	}

	/**
	 * Gets the faces mapping.
	 * 
	 * @param context
	 *            the context
	 * 
	 * @return the faces mapping
	 */
	public String getFacesMapping(final FacesContext context) {
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
	 * Gets the request uri.
	 * 
	 * @param context
	 *            the context
	 * 
	 * @return the request uri
	 */
	public abstract String getRequestURI(final FacesContext context);

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
	public boolean isPrefixMapped(final String mapping) {
		return mapping.charAt(0) == '/';
	}

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
	public abstract void writeResponse(final FacesContext context,
			final String contentType, final byte[] data) throws IOException;

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
	private String getMappingForRequest(final String servletPath,
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

}
