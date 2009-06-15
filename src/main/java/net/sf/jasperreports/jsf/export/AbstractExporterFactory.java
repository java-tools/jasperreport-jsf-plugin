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

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.spi.ExporterFactory;
import net.sf.jasperreports.jsf.validation.ReportValidator;
import net.sf.jasperreports.jsf.validation.ReportValidatorFactory;
import net.sf.jasperreports.jsf.validation.ValidationException;

public abstract class AbstractExporterFactory implements ExporterFactory {

	public Exporter createExporter(final FacesContext context,
			final UIReport report) throws ExporterException {
		processValidators(context, report);
		return doCreateExporter(context, report);
	}

	protected abstract Exporter doCreateExporter(FacesContext context,
			UIReport report) throws ExporterException;

	protected void processValidators(final FacesContext context,
			final UIReport report) throws ValidationException {
		final ReportValidator validator = ReportValidatorFactory.getValidator(
				context, report);
		if (validator != null) {
			validator.validate(context, report);
		}
	}

}