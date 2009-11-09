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
package net.sf.jasperreports.jsf.validation.providers;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.export.providers.Graphics2DExporter;
import net.sf.jasperreports.jsf.validation.MissedAttributeException;
import net.sf.jasperreports.jsf.validation.ReportValidatorBase;
import net.sf.jasperreports.jsf.validation.ValidationException;

public class Graphics2DExporterValidator extends ReportValidatorBase {

	@Override
	protected void doValidate(FacesContext context, UIReport report)
			throws ValidationException {
		super.doValidate(context, report);
		if(null == ((UIComponent) report).getAttributes()
				.get(Graphics2DExporter.ATTR_GRAPHICS_2D)) {
			throw new MissedAttributeException(report.getFormat() + 
					" : " + Graphics2DExporter.ATTR_GRAPHICS_2D);
		}
	}

}
