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
package net.sf.jasperreports.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 *
 * @author antonio.alonso
 */
public abstract class OutputReportTag extends ReportTag {

    /** The format. */
    private ValueExpression format;

    /**
     * Sets the format.
     *
     * @param type the new format
     */
    public void setFormat(final ValueExpression type) {
        format = type;
    }

    // TagSupport

    @Override
    public void release() {
        super.release();
        format = null;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringAttribute(component, "format", format);
    }

}
