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
