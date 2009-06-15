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
package net.sf.jasperreports.jsf.export;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * The Interface Exporter.
 */
public interface Exporter {

	/**
	 * MIME type of the report generated using this exporter instance
	 * 
	 * @return the response MIME type
	 */
	public String getContentType();

	/**
	 * Export.
	 * 
	 * @param context
	 *            the context
	 * @param print
	 *            the print
	 * @param stream
	 *            the stream
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ExporterException
	 *             the exporter exception
	 */
	public void export(final FacesContext context, final JasperPrint print,
			final OutputStream stream) throws IOException, ExporterException;

}
