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
package net.sf.jasperreports.jsf.export.providers;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractJRExporter;

public final class DocxExporter extends AbstractJRExporter {

    public static final String CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    //public static final String ATTR_FLEXIBLE_ROW_HEIGHT = "FLEXIBLE_ROW_HEIGHT";
    public static final String ATTR_FRAMES_AS_NESTED_TABLES = "FRAMES_AS_NESTED_TABLES";

    protected DocxExporter(final UIComponent component) {
        super(component);
    }

    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    protected JRExporter createJRExporter(final FacesContext context) {
        final JRDocxExporter exporter = new JRDocxExporter();
        setParameterUsingAttribute(exporter,
                JRDocxExporterParameter.FRAMES_AS_NESTED_TABLES, ATTR_FRAMES_AS_NESTED_TABLES);
        return exporter;
    }
}
