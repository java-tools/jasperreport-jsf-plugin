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
package net.sf.jasperreports.jsf.export.providers;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PdfExporter.
 */
public class PdfExporter extends AbstractExporter {

    /** The Constant ATTR_IS_COMPRESSED. */
    public static final String ATTR_IS_COMPRESSED = "IS_COMPRESSED";

    /** The Constant ATTR_IS_ENCRYPTED. */
    public static final String ATTR_IS_ENCRYPTED = "IS_ENCRYPTED";

    /* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.export.Exporter#createJRExporter(javax.faces.context.FacesContext)
     */
    @Override
    protected JRExporter createJRExporter(final FacesContext context) {
        final JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRPdfExporterParameter.IS_COMPRESSED,
                getComponent().getAttributes().get(ATTR_IS_COMPRESSED));
        exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED,
                getComponent().getAttributes().get(ATTR_IS_ENCRYPTED));
        return exporter;
    }

}