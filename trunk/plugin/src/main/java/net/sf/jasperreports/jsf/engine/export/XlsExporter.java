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
package net.sf.jasperreports.jsf.engine.export;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.context.ContentType;

/**
 * Exporter implementation which generates a XLS report.
 *
 * @author A. Alonso Dominguez
 */
public final class XlsExporter extends AbstractXlsExporter {

    /** The MIME type for this exporter. */
    public static final ContentType CONTENT_TYPE = 
    	new ContentType("application/vnd.ms-excel");

    /**
     * @see net.sf.jasperreports.jsf.engine.Exporter#getContentType()
     */
    public ContentType getContentType() {
        return CONTENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.export.DefaultExporter#createJRExporter(
     * javax.faces.context.FacesContext)
     */
    @Override
    protected JRXlsAbstractExporter createJRXlsExporter(
            final FacesContext context, final UIOutputReport component) {
        return new JRXlsExporter();
    }
}
