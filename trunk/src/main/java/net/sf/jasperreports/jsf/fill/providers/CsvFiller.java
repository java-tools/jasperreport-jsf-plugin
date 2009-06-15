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
package net.sf.jasperreports.jsf.fill.providers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractFiller;
import net.sf.jasperreports.jsf.fill.FillerException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;
import net.sf.jasperreports.jsf.spi.ResourceLoader;

/**
 * The Class CsvFiller.
 */
public final class CsvFiller extends AbstractFiller {

	protected CsvFiller(final UIDataSource dataSource) {
		super(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jasperreports.jsf.fill.Filler#doFill(javax.faces.context.FacesContext
	 * , java.io.InputStream, java.util.Map)
	 */
	@Override
	protected JasperPrint doFill(final FacesContext context,
			final InputStream reportStream, final Map<String, Object> params)
			throws FillerException {
		JRDataSource dataSource = null;
		InputStream dsStream = null;

		final Object value = getDataSourceComponent().getValue();
		if(value instanceof URL) {
			try {
				dsStream = ((URL) value).openStream();
			} catch (IOException e) {
				throw new FillerException(e);
			}
		} else if(value instanceof String) {
			try {
				Resource resource = ResourceLoader.getResource(
						context, (String) value);
				dsStream = resource.getInputStream();
			} catch (ResourceException e) {
				throw new FillerException(e);
			} catch (IOException e) {
				throw new FillerException(e);
			}
		} else if(value instanceof File) {
			try {
				dataSource = new JRCsvDataSource((File) value);
			} catch (final FileNotFoundException e) {
				throw new FillerException(e);
			}
		}

		if (dataSource == null) {
			if (dsStream == null) {
				throw new FillerException("CSV datasource needs a valid File, URL or string");
			} else {
				dataSource = new JRCsvDataSource(dsStream);
			}
		}
		
		try {
			return JasperFillManager.fillReport(reportStream, params,
					dataSource);
		} catch (final JRException e) {
			throw new FillerException(e);
		} finally {
			if(dsStream != null) {
				try {
					dsStream.close();
					dsStream = null;
				} catch(IOException e) { }
			}
		}
	}

}
