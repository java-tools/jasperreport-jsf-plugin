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
package net.sf.jasperreports.jsf.validation.providers;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.engine.databroker.JdbcDataBrokerFactory;
import net.sf.jasperreports.jsf.validation.DataBrokerValidatorBase;
import net.sf.jasperreports.jsf.validation.MissingAttributeException;
import net.sf.jasperreports.jsf.validation.ValidationException;

public class JdbcDataSourceValidator extends DataBrokerValidatorBase {

    /** The Constant REQUIRED_DATASOURCE_ATTRS. */
    public static final String[] REQUIRED_DATASOURCE_ATTRS = {
        JdbcDataBrokerFactory.ATTR_DRIVER_CLASS_NAME};

    @Override
    public void validate(final FacesContext context,
            final UIDataBroker component) throws ValidationException {
        super.validate(context, component);
        for (final String attr : REQUIRED_DATASOURCE_ATTRS) {
            if (null == component.getAttributes().get(attr)) {
                throw new MissingAttributeException(
                        component.getType() + " : " + attr);
            }
        }
    }
}
