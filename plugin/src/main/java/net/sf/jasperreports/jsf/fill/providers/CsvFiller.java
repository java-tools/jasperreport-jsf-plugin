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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractJRDataSourceFiller;
import net.sf.jasperreports.jsf.fill.FillerException;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceException;
import net.sf.jasperreports.jsf.spi.ResourceLoader;

/**
 * The Class CsvFiller.
 */
public final class CsvFiller extends AbstractJRDataSourceFiller {

	private static final Logger logger = Logger.getLogger(BeanFiller.class
			.getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

	private InputStream dataSourceStream = null;
	private boolean closeStream = true;

	protected CsvFiller(final UIDataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected JRDataSource getJRDataSource(final FacesContext context)
			throws FillerException {
		JRDataSource dataSource = null;

		final Object value = getDataSourceComponent().getValue();
		if (value == null) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, "JRJSF_0020",
						getDataSourceComponent().getClientId(context));
			}
			dataSource = new JREmptyDataSource();
		} else if (value instanceof URL) {
			try {
				dataSourceStream = ((URL) value).openStream();
			} catch (final IOException e) {
				throw new FillerException(e);
			}
		} else if (value instanceof InputStream) {
			dataSourceStream = (InputStream) value;
			closeStream = false;
		} else if (value instanceof String) {
			try {
				final Resource resource = ResourceLoader.getResource(context,
						(String) value);
				dataSourceStream = resource.getInputStream();
			} catch (final ResourceException e) {
				throw new FillerException(e);
			} catch (final IOException e) {
				throw new FillerException(e);
			}
		} else if (value instanceof File) {
			try {
				dataSource = new JRCsvDataSource((File) value);
			} catch (final FileNotFoundException e) {
				throw new FillerException(e);
			}
		}

		if (dataSource == null) {
			if (dataSourceStream == null) {
				throw new FillerException("CSV datasource needs a valid File, "
						+ "URL, InputStream or string");
			} else {
				dataSource = new JRCsvDataSource(dataSourceStream);
			}
		}

		return dataSource;
	}

	@Override
	protected void closeDataSource(final JRDataSource dataSource)
			throws FillerException {
		if (dataSourceStream != null && closeStream) {
			try {
				dataSourceStream.close();
			} catch (final IOException e) {
			}
		}
		dataSourceStream = null;
	}

}
