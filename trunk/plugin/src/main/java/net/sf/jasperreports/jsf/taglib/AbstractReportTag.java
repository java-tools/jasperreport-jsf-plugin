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

import net.sf.jasperreports.jsf.component.UIReport;

/**
 * The Class AbstractReportTag.
 */
public abstract class AbstractReportTag extends UIComponentELTag {

    /** The data source. */
    private ValueExpression dataSource;

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
    public void setDataSource(final ValueExpression dataSource) {
        this.dataSource = dataSource;
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
        dataSource = null;
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

        final UIReport jreport = (UIReport) component;
        if (dataSource != null) {
            if (dataSource.isLiteralText()) {
                jreport.setDataSource(dataSource.getExpressionString());
            } else {
                component.setValueExpression("dataSource", dataSource);
            }
        }

        if (name != null) {
            if (name.isLiteralText()) {
                jreport.setName(name.getExpressionString());
            } else {
                component.setValueExpression("name", name);
            }
        }

        if (path != null) {
            if (path.isLiteralText()) {
                jreport.setPath(path.getExpressionString());
            } else {
                component.setValueExpression("report", path);
            }
        }

        if (subreportDir != null) {
            if (subreportDir.isLiteralText()) {
                jreport.setSubreportDir(subreportDir.getExpressionString());
            } else {
                component.setValueExpression("subreportDir", subreportDir);
            }
        }

        if (format != null) {
            if (format.isLiteralText()) {
                jreport.setFormat(format.getExpressionString());
            } else {
                component.setValueExpression("format", format);
            }
        }
    }
}
