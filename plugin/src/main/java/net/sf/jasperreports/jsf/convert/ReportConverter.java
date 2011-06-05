/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.convert;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.jsf.component.UIReport;

/**
 * Report converter interface.
 *
 * @author A. Alonso Dominguez
 */
public interface ReportConverter {

    /**
     * Obtains a <tt>JasperReport</tt> instance from the value
     * received as a parameter.
     *
     * @param context current faces' context.
     * @param component the faces' component asking for conversion.
     * @param value the value that must be converted.
     * @return a <tt>JasperReport</tt> instance.
     * @throws ConverterException if value can't be converted.
     */
    public JasperReport convertFromValue(FacesContext context,
            UIReport component, Object value)
    throws ConverterException;

}
