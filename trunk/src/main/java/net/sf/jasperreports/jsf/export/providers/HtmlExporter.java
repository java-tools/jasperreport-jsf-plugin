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
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractExporter;

/**
 * The Class HtmlExporter.
 */
public class HtmlExporter extends AbstractExporter {

    /** The Constant ATTR_HTML_HEADER. */
    public static final String ATTR_HTML_HEADER = "HTML_HEADER";

    /** The Constant ATTR_HTML_FOOTER. */
    public static final String ATTR_HTML_FOOTER = "HTML_FOOTER";

    /** The Constant ATTR_IMAGES_DIR. */
    public static final String ATTR_IMAGES_DIR = "IMAGES_DIR";

    /** The Constant ATTR_IMAGES_DIR_NAME. */
    public static final String ATTR_IMAGES_DIR_NAME = "IMAGES_DIR_NAME";

    /** The Constant ATTR_IMAGES_URI. */
    public static final String ATTR_IMAGES_URI = "IMAGES_URI";

    /* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.export.Exporter#createJRExporter(javax.faces.context.FacesContext)
     */
    @Override
    protected JRExporter createJRExporter(final FacesContext context) {
        final JRHtmlExporter exporter = new JRHtmlExporter();
        exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER,
                getComponent().getAttributes().get(ATTR_HTML_HEADER));
        exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER,
                getComponent().getAttributes().get(ATTR_HTML_FOOTER));
        exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR,
                getComponent().getAttributes().get(ATTR_IMAGES_DIR));
        exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME,
                getComponent().getAttributes().get(ATTR_IMAGES_DIR_NAME));
        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
                getComponent().getAttributes().get(ATTR_IMAGES_URI));
        return exporter;
    }

}
