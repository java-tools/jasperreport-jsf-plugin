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
package net.sf.jasperreports.jsf.spi;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.export.Exporter;
import net.sf.jasperreports.jsf.export.ExporterException;
import net.sf.jasperreports.jsf.export.ExporterNotFoundException;
import net.sf.jasperreports.jsf.util.Util;

/**
 * A factory for creating Exporter objects.
 */
public final class ExporterLoader {

	/** The exporter cache map. */
	private static final Map<String, Class<ExporterFactory>> exporterCacheMap = Util
			.loadServiceMap(ExporterFactory.class);

	public static Set<String> getAvailableExportFormats() {
		return Collections.unmodifiableSet(exporterCacheMap.keySet());
	}

	/**
	 * Gets the single instance of Exporter.
	 * 
	 * @param report
	 *            the report
	 * @param context
	 *            the context
	 * 
	 * @return single instance of Exporter
	 * 
	 * @throws ExporterException
	 *             the exporter exception
	 */
	public static Exporter getExporter(final FacesContext context,
			final UIReport report) throws ExporterException {
		if (!(report instanceof UIComponent)) {
			throw new IllegalArgumentException();
		}

		final ExporterFactory factory = getExporterFactory(context, report);
		return factory.createExporter(context, report);
	}

	private static ExporterFactory getExporterFactory(
			final FacesContext context, final UIReport report)
			throws JRFacesException {
		final Class<ExporterFactory> exporterFactoryClass = exporterCacheMap
				.get(report.getFormat());
		if (exporterFactoryClass == null) {
			throw new ExporterNotFoundException(report.getFormat());
		}

		try {
			return exporterFactoryClass.newInstance();
		} catch (final Exception e) {
			throw new JRFacesException(e);
		}
	}

	/**
	 * Instantiates a new exporter factory.
	 */
	private ExporterLoader() {
	}

}