package net.sf.jasperreports.jsf;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public final class Util {

	private static final String INVOCATION_PATH =
		"net.sf.jasperreports.jsf.INVOCATION_PATH";
	
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
	
	private Util() { }
	
}
