package net.sf.jasperreports.jsf.util;

import java.io.InputStream;
import java.net.URL;

public class ClasspathResourceLoader extends ResourceLoader {

	public static final String CLASSPATH_PREFIX = "classpath:";
	
	private ClassLoader classLoader;
	
	public ClasspathResourceLoader(ClassLoader classLoader) {
		if(classLoader == null) {
			throw new NullPointerException();
		}
		this.classLoader = classLoader;
	}
	
	public String getRealPath(String name) {
		URL resource = getResource(name);
		if(resource == null) {
			return null;
		}
		return resource.getPath();
	}

	public URL getResource(String name) {
		if(name == null || name.length() == 0) {
			throw new IllegalArgumentException();
		}
		if(name.startsWith(CLASSPATH_PREFIX)) {
			name = name.substring(CLASSPATH_PREFIX.length());
		}
		return classLoader.getResource(name);
	}
	
	public InputStream getResourceAsStream(String name) {
		if(name == null || name.length() == 0) {
			throw new IllegalArgumentException();
		}
		if(name.startsWith(CLASSPATH_PREFIX)) {
			name = name.substring(CLASSPATH_PREFIX.length());
		}
		return classLoader.getResourceAsStream(name);
	}
	
}
