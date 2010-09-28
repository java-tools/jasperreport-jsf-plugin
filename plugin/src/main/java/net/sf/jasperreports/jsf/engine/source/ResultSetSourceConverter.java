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
package net.sf.jasperreports.jsf.engine.source;

import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.jsf.convert.DefaultSourceConverter;
import net.sf.jasperreports.jsf.engine.Source;

/**
 *
 * @author aalonsodominguez
 */
public class ResultSetSourceConverter extends DefaultSourceConverter {

    private static final Logger logger = Logger.getLogger(
            ResultSetSourceConverter.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    @Override
    protected Source createSource(FacesContext context,
            UIComponent component, Object value) {
        JRDataSource dataSource;
        final ResultSet rs = (ResultSet) value;
        if (rs == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else {
            dataSource = new JRResultSetDataSource(rs);
        }
        return new JRDataSourceWrapper(dataSource);
    }

}
