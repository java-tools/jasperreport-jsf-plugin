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
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.ContextClassLoaderObjectInputStream;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.validation.ValidationException;
import net.sf.jasperreports.jsf.validation.Validator;

/**
 * The Class UIReport.
 */
public abstract class UIReport extends UIComponentBase {

    /** The Constant COMPONENT_FAMILY. */
    public static final String COMPONENT_FAMILY =
            Constants.PACKAGE_PREFIX + ".Report";

    private static final Logger logger = Logger.getLogger(
            UIReport.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    /** The data source. */
    private Object source;

    private String name;
    private boolean valid = true;
    private Object value;
    private boolean valueSet = false;

    private SourceConverter converter;
    private Validator validator;

    private Source submittedSource;
    private JasperReport submittedReport;

    /**
     * Instantiates a new uI report implementor.
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
    public Object getSource() {
        if (source != null) {
            return source;
        }
        final ValueExpression ve = getValueExpression("source");
        if (ve != null) {
            try {
                return ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return source;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setDataSource(java.lang.String
     * )
     */
    public void setSource(final Object dataSource) {
        this.source = dataSource;
    }

    public String getName() {
        if (name != null) {
            return name;
        }
        final ValueExpression ve = getValueExpression("name");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
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

    public Object getValue() {
        if (valueSet) {
            return value;
        }
        ValueExpression ve = getValueExpression("value");
        if (ve != null) {
            try {
                return ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return value;
        }
    }

    public void setValue(Object value) {
        this.value = value;
        this.valueSet = true;
    }

    public boolean isLocalValueSet() {
        return valueSet;
    }

    public SourceConverter getConverter() {
        if (converter != null) {
            return converter;
        }
        ValueExpression ve = getValueExpression("converter");
        if (ve != null) {
            try {
                return (SourceConverter) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return converter;
        }
    }

    public void setConverter(SourceConverter converter) {
        this.converter = converter;
    }

    public Validator getValidator() {
        if (validator != null) {
            return validator;
        }
        ValueExpression ve = getValueExpression("validator");
        if (ve != null) {
            try {
                return (Validator) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return validator;
        }
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Source getSubmittedSource() {
        return submittedSource;
    }

    public void setSubmittedSource(Source submittedSource) {
        this.submittedSource = submittedSource;
    }

    public JasperReport getSubmittedReport() {
        return submittedReport;
    }

    public void setSubmittedReport(JasperReport submittedReport) {
        this.submittedReport = submittedReport;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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
        source = (String) values[1];
        name = (String) values[2];
        value = values[3];
        valueSet = ((Boolean) values[4]).booleanValue();
        converter = (SourceConverter) values[5];
        valid = ((Boolean) values[6]).booleanValue();
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
        final Object[] values = new Object[7];
        values[0] = super.saveState(context);
        values[1] = source;
        values[2] = name;
        values[3] = value;
        values[4] = valueSet;
        values[5] = converter;
        values[6] = valid;
        return values;
    }

    public void resetValue() {
        this.value = null;
        this.valueSet = false;
        this.valid = true;
        this.submittedSource = null;
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        if (!isRendered()) {
            return;
        }

        resetValue();
        super.processDecodes(context);

        try {
            decodeSource(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
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
        executeValidate(context);
    }

    protected void decodeSource(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        SourceConverter aConverter = getConverter();
        if (aConverter == null) {
            aConverter = getJRFacesContext()
                    .createSourceConverter(context, this);
        }

        Source aSource = aConverter.convertFromValue(
                context, this, getSource());
        setSubmittedSource(aSource);
    }

    protected void decodeReport(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        Object aValue = getValue();
        if (aValue == null) {
            return;
        }

        JasperReport aReport;
        if (aValue instanceof JasperReport) {
            aReport = (JasperReport) aValue;
        } else {
            String valueStr;
            if (aValue instanceof String) {
                valueStr = (String) aValue;
            } else {
                valueStr = aValue.toString();
            }

            Resource resource = getJRFacesContext().createResource(
                    context, this, (String) valueStr);
            
            ObjectInputStream ois = null;
            try {
                ois = new ContextClassLoaderObjectInputStream(
                        resource.getInputStream());
                aReport = (JasperReport) ois.readObject();
            } catch (IOException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    LogRecord record = new LogRecord(
                            Level.SEVERE, "JRJSF_0033");
                    record.setParameters(new Object[]{
                                resource.getName()});
                    record.setThrown(e);
                    logger.log(record);
                }
                throw new FacesException(e);
            } catch (ClassNotFoundException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    LogRecord record = new LogRecord(
                            Level.SEVERE, "JRJSF_0034");
                    record.setParameters(new Object[]{
                                resource.getName()});
                    record.setThrown(e);
                    logger.log(record);
                }
                throw new FacesException(e);
            }
        }

        setSubmittedReport(aReport);
    }

    public void validate(FacesContext context)
            throws ValidationException {
        if (context == null) {
            throw new NullPointerException();
        }

        Validator aValidator = getValidator();
        if (aValidator == null) {
            aValidator = getJRFacesContext()
                .createValidator(context, this);
        }

        if (aValidator != null) {
            try {
                aValidator.validate(context, this);
            } catch (ValidationException e) {
                setValid(false);
                throw e;
            }
        }
    }

    protected JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
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
