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
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.spi.ValidatorLoader;
import net.sf.jasperreports.jsf.validation.ValidationException;
import net.sf.jasperreports.jsf.validation.Validator;

/**
 * The Class UIReportImplementor.
 */
public class UIReport extends UIComponentBase {

    /** The Constant COMPONENT_FAMILY. */
    public static final String COMPONENT_FAMILY =
            Constants.PACKAGE_PREFIX + ".Report";

    /** The data source. */
    private String dataSource;

    private boolean immediate;
    private boolean immediateSet = false;

    private String name;
    /** The path. */
    private String path;
    /** The subreport dir. */
    private String subreportDir;
    /** The format. */
    private String format;

    /**
     * Instantiates a new uI report implementor.
     *
     * @param facade the facade
     */
    public UIReport() {
        super();
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getDataSource()
     */
    public String getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        final ValueExpression ve = getValueExpression("dataSource");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return dataSource;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setDataSource(java.lang.String
     * )
     */
    public void setDataSource(final String dataSource) {
        this.dataSource = dataSource;
    }

    public String getName() {
        if (name != null) {
            return name;
        }
        final ValueExpression ve = getValueExpression("name");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return name;
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getPath()
     */
    public String getPath() {
        if (path != null) {
            return path;
        }
        final ValueExpression ve = getValueExpression("path");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return path;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setPath(java.lang.String)
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getSubreportDir()
     */
    public String getSubreportDir() {
        if (subreportDir != null) {
            return subreportDir;
        }
        final ValueExpression ve = getValueExpression("subreportDir");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return subreportDir;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setSubreportDir(java.lang
     * .String)
     */
    public void setSubreportDir(final String subreportDir) {
        this.subreportDir = subreportDir;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.component.UIReport#getFormat()
     */
    public String getFormat() {
        if (format != null) {
            return format;
        }
        final ValueExpression ve = getValueExpression("format");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return format;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setFormat(java.lang.String)
     */
    public void setFormat(final String type) {
        format = type;
    }

    public boolean isImmediate() {
        if (immediateSet) {
            return immediate;
        }
        ValueExpression ve = getValueExpression("immediate");
        if (ve != null) {
            try {
                return (Boolean) ve.getValue(getFacesContext().getELContext());
            } catch(ELException e) {
                throw new FacesException(e);
            }
        } else {
            return immediate;
        }
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
        this.immediateSet = true;
    }

    // UIReport encode methods
    public void encodeContent(final FacesContext context) throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeContent(context, this);
    }

    public void encodeHeaders(final FacesContext context) throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeHeaders(context, this);
    }

    /*
     * (non-Javadoc)
     *
     * @seejavax.faces.component.StateHolder#restoreState(javax.faces.context.
     * FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        dataSource = (String) values[1];
        path = (String) values[2];
        subreportDir = (String) values[3];
        format = (String) values[4];
        name = (String) values[5];
        immediate = ((Boolean) values[6]).booleanValue();
        immediateSet = ((Boolean) values[7]).booleanValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext
     * )
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[8];
        values[0] = super.saveState(context);
        values[1] = dataSource;
        values[2] = path;
        values[3] = subreportDir;
        values[4] = format;
        values[5] = name;
        values[6] = immediate;
        values[7] = immediateSet;
        return values;
    }

    public void validate(FacesContext context) throws ValidationException {
        if (context == null) {
            throw new NullPointerException();
        }

        final Validator validator = ValidatorLoader.getValidator(context, this);
        if (validator != null) {
            validator.validate(context, this);
        }
    }

    @Override
    public void processDecodes(FacesContext context) {
       if (context == null) {
            throw new NullPointerException();
        }

        if (!isRendered()) {
            return;
        }
        super.processDecodes(context);
        if (isImmediate()) {
            executeValidate(context);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (!isRendered()) {
            return;
        }
        super.processValidators(context);
        if (!isImmediate()) {
            executeValidate(context);
        }
    }

    protected void executeValidate(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        try {
            validate(context);
        } catch(ValidationException e) {
            context.renderResponse();
            throw e;
        }
    }
    
}
