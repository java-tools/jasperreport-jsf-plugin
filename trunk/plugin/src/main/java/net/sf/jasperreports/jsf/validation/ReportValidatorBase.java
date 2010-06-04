/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.validation;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.JRFacesContext;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

public class ReportValidatorBase extends ReportValidator {

    @Override
    protected void doValidate(final FacesContext context, final UIReport report)
            throws ValidationException {
        final JRFacesContext jrContext = JRFacesContext.getInstance(context);
        String format = getStringAttribute(report, "format", null);
        if (format != null && format.length() > 0) {
            if (!jrContext.getAvailableExportFormats().contains(format)) {
                throw new IllegalOutputFormatException(format);
            }
        }
    }
}
