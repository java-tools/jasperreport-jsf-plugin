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
import javax.faces.webapp.UIComponentELTag;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * The Class AbstractReportTag.
 */
public abstract class AbstractReportTag extends UIComponentELTag {

    /** The data source. */
    private ValueExpression reportSource;

    private ValueExpression name;
    /** The path. */
    private ValueExpression path;
    /** The subreport dir. */
    private ValueExpression subreportDir;
    /** The format. */
    private ValueExpression format;

    /**
     * Sets the data source.
     *
     * @param dataSource
     *            the new data source
     */
    public void setReportSource(final ValueExpression dataSource) {
        this.reportSource = dataSource;
    }

    public void setName(final ValueExpression name) {
        this.name = name;
    }

    /**
     * Sets the path.
     *
     * @param report
     *            the new path
     */
    public void setPath(final ValueExpression report) {
        path = report;
    }

    /**
     * Sets the subreport dir.
     *
     * @param subreportDir
     *            the new subreport dir
     */
    public void setSubreportDir(final ValueExpression subreportDir) {
        this.subreportDir = subreportDir;
    }

    /**
     * Sets the format.
     *
     * @param type
     *            the new format
     */
    public void setFormat(final ValueExpression type) {
        format = type;
    }

    // TagSupport

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentELTag#release()
     */
    @Override
    public void release() {
        super.release();
        reportSource = null;
        path = null;
        subreportDir = null;
        format = null;
        name = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.webapp.UIComponentELTag#setProperties(javax.faces.component
     * .UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);

        setStringAttribute(component, "name", name);
        setStringAttribute(component, "format", format);
        setStringAttribute(component, "path", path);
        setStringAttribute(component, "reportSource", reportSource);
        setStringAttribute(component, "subreportDir", subreportDir);
    }
}
