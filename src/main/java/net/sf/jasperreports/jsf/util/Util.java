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

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletContext;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class Util {

	private static final String INVOCATION_PATH =
		"net.sf.jasperreports.jsf.INVOCATION_PATH";
	
	private static final String PORTLET_CLASS = 
		"javax.portlet.Portlet";
	
	private static final String PORTLET_RESOURCEURL_CLASS =
		"javax.portlet.ResourceURL";
	
	public static final boolean PORTLET_AVAILABLE;
	
	public static final String PORTLET_VERSION;
	
	static {
		boolean portletAvailable = false;
		try {
			Class.forName(PORTLET_CLASS);
			portletAvailable = true;
		} catch(ClassNotFoundException e) {
			portletAvailable = false;
		}
		
		String portletVersion = null;
		try {
			Class.forName(PORTLET_RESOURCEURL_CLASS);
			portletVersion = "2.0";
		} catch(ClassNotFoundException e) {
			portletVersion = (portletAvailable ? "1.0" : null);
		}
		
		PORTLET_AVAILABLE = portletAvailable;
		PORTLET_VERSION = portletVersion;
	}
	
	public static ClassLoader getClassLoader(Object fallback) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(loader == null) {
			if(fallback != null) {
				loader = fallback.getClass().getClassLoader();
			} else {
				loader = Util.class.getClassLoader();
			}
		}
		return loader;
	}
	
	public static String getFacesMapping(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        // Check for a previously stored mapping   
        ExternalContext extContext = context.getExternalContext();
        String mapping =
              (String) extContext.getRequestMap().get(INVOCATION_PATH);

        if (mapping == null) {

            Object request = extContext.getRequest();
            String servletPath = null;
            String pathInfo = null;

            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
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
    
	public static boolean isPortletContext(FacesContext facesContext) {
		if(!PORTLET_AVAILABLE) return false;
		
		Object context = facesContext.getExternalContext().getContext();
		return (context instanceof PortletContext);
	}
	
	public static String getRequestURI(FacesContext context) {
		if(isPortletContext(context)) {
			if("2.0".equals(PORTLET_VERSION)) {
				ResourceRequest request = (ResourceRequest) context
					.getExternalContext().getRequest();
				return request.getResourceID();
			} else {
				return null;
			}
		} else {
			HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
			return request.getRequestURI();
		}
	}
	
    /**
     * <p>Returns true if the provided <code>url-mapping</code> is
     * a prefix path mapping (starts with <code>/</code>).</p>
     *
     * @param mapping a <code>url-pattern</code>
     * @return true if the mapping starts with <code>/</code>
     */
    public static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }
    
    public static void writeResponse(FacesContext context, 
    		String contentType, byte[] data) 
    throws IOException {
    	if(isPortletContext(context)) {
    		if("2.0".equals(PORTLET_VERSION)) {
    			ResourceResponse response = (ResourceResponse) context
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
    		HttpServletResponse response = (HttpServletResponse) context
    				.getExternalContext().getResponse();
    		response.setContentType(contentType);
			response.setContentLength(data.length);
			response.getOutputStream().write(data);
			
			response.setHeader("Cache-Type", "no-cache");
			response.setHeader("Expires", "0");
    	}
    }
    
    /**
     * <p>Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.</p>
     *
     * @param servletPath the servlet path of the request
     * @param pathInfo    the path info of the request
     *
     * @return the appropriate mapping based on the current request
     *
     * @see HttpServletRequest#getServletPath()
     */
    private static String getMappingForRequest(String servletPath, String pathInfo) {

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
