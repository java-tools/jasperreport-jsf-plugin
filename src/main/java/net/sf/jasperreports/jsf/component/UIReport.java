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
package net.sf.jasperreports.jsf.component;

import java.io.IOException;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

/**
 * The Interface UIReport.
 */
public interface UIReport extends StateHolder {

	/** The Constant COMPONENT_FAMILY. */
	public static final String COMPONENT_FAMILY = "net.sf.jasperreports.Report";

	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public String getDataSource();

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource
	 *            the new data source
	 */
	public void setDataSource(String dataSource);

	public String getName();
	
	public void setName(String name);
	
	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public String getPath();

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(String path);

	/**
	 * Gets the subreport dir.
	 * 
	 * @return the subreport dir
	 */
	public String getSubreportDir();

	/**
	 * Sets the subreport dir.
	 * 
	 * @param subreportDir
	 *            the new subreport dir
	 */
	public void setSubreportDir(String subreportDir);

	/**
	 * Gets the format.
	 * 
	 * @return the format
	 */
	public String getFormat();

	/**
	 * Sets the format.
	 * 
	 * @param format
	 *            the new format
	 */
	public void setFormat(String format);

	public void encodeHeaders(FacesContext context) throws IOException;
	
}
