/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.fill;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.component.UIReport;

// TODO: Auto-generated Javadoc
/**
 * The Interface Filler.
 */
public interface Filler {

    /**
     * Gets the data source component.
     * 
     * @return the data source component
     */
    public UIDataSource getDataSourceComponent();

    /**
     * Sets the data source component.
     * 
     * @param dataSourceComponent the new data source component
     */
    public void setDataSourceComponent(final UIDataSource dataSourceComponent);

    /**
     * Gets the required data source attributes.
     * 
     * @return the required data source attributes
     */
    public String[] getRequiredDataSourceAttributes();

    /**
     * Fill.
     * 
     * @param context the context
     * @param report the report
     * 
     * @return the jasper print
     * 
     * @throws FillerException the filler exception
     */
    public JasperPrint fill(final FacesContext context, final UIReport report)
            throws FillerException;

}
