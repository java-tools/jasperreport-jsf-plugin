package net.sf.jasperreports.jsf.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

public class ContextResourceLoader extends ResourceLoader {

	private ExternalContext context;
	
	public ContextResourceLoader(ExternalContext servletContext) {
		if(servletContext == null) {
			throw new NullPointerException();
		}
		this.context = servletContext;
	}
	
	public String getRealPath(String name) {
		ServletContext servletContext = (ServletContext) context.getContext();
		return servletContext.getRealPath(name);
	}
	
	public URL getResource(String name) 
	throws MalformedURLException {
		return context.getResource(name);
	}
	
	public InputStream getResourceAsStream(String name) {
		return context.getResourceAsStream(name);
	}
	
}
