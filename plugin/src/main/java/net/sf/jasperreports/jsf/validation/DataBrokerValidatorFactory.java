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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.util.Services;

public final class DataBrokerValidatorFactory {

    private static final Map<String, DataBrokerValidator> validatorCacheMap =
            Services.map(DataBrokerValidator.class);

    public static DataBrokerValidator createValidator(final FacesContext context,
            final UIReportSource component) throws ValidationException {
        DataBrokerValidator result = null;
        result = validatorCacheMap.get(component.getType());
        if (result == null) {
            result = validatorCacheMap.get(null);
        }
        return result;
    }
}
