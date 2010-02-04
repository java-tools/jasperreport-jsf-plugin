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

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.spi.Services;
import net.sf.jasperreports.jsf.spi.ValidatorFactory;

public final class DataSourceValidatorFactory implements ValidatorFactory {

    private static final Map<String, DataSourceValidator> validatorCacheMap =
            Services.map(DataSourceValidator.class);

    public boolean acceptsComponent(final UIComponent component) {
        return (component instanceof UIDataSource);
    }

    public DataSourceValidator createValidator(final FacesContext context,
            final UIComponent component) throws ValidationException {
        if (!(component instanceof UIDataSource)) {
            throw new IllegalArgumentException("");
        }

        final UIDataSource dataSource = (UIDataSource) component;
        DataSourceValidator result = null;
        result = validatorCacheMap.get(dataSource.getType());
        if (result == null) {
            result = validatorCacheMap.get(null);
        }
        return result;
    }
}
