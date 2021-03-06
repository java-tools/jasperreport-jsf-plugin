/*
 * JaspertReports JSF Plugin Copyright (C) 2012 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.engine;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIOutputReport;

/**
 * The Filler interface.
 *
 * @author A. Alonso Dominguez
 */
public interface Filler {

    /**
     * Process the report component and fill it with the
     * external data if needed.
     *
     * @param context current faces' context.
     * @param component the report component.
     *
     * @throws FillerException if some error happens when processing the data.
     */
    public void fill(final FacesContext context, final UIOutputReport component)
            throws FillerException;
    
}
