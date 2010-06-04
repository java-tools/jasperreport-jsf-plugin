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
package net.sf.jasperreports.jsf.component;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

/**
 *
 * @author antonio.alonso
 */
public class UIOutputReport extends UIReport {

    private String format;

    private Exporter exporter;

    public UIOutputReport() {
        super();
    }

    public String getFormat() {
        if (format != null) {
            return format;
        }
        final ValueExpression ve = getValueExpression("format");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return format;
        }
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public Exporter getExporter() {
        if (exporter != null) {
            return exporter;
        }
        ValueExpression ve = getValueExpression("exporter");
        if (ve != null) {
            try {
                return (Exporter) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return exporter;
        }
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }

    // UIOutputReport encode methods

    public void encodeContent(final FacesContext context) throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeContent(context, this);
    }

    public void encodeHeaders(final FacesContext context) throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeHeaders(context, this);
    }

    public void updateModel(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (isLocalValueSet() || !isValid()) {
            return;
        }

        Object providedValue = getValue();
        if (providedValue == null) {
            JRFacesContext jrContext = getJRFacesContext();
            ValueExpression ve = getValueExpression("value");
            if (ve != null) {

            }
        }
    }

}
