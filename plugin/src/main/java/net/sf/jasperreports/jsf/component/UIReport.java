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
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.ContextClassLoaderObjectInputStream;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.validation.ReportValidatorBase;

/**
 * Base class for report components.
 *
 * @author A. Alonso Dominguez
 */
public class UIReport extends UIComponentBase {

    /** Name used to identify the reports component family. */
    public static final String COMPONENT_FAMILY =
            Constants.PACKAGE_PREFIX + ".Report";

    /** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            UIReport.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    /** The source object from where obtain the report's data. */
    private Object source;

    /** The report's name. */
    private String name;

    /**
     * Valid flag to determine if the component has been
     * correclty configured.
     */
    private boolean valid = true;

    /** Report value, which is the report itself. */
    private Object value;
    /**
     * Boolean flag used to know if report value has been
     * programatically set.
     */
    private boolean valueSet = false;

    /**
     * Converter instance used to interpret the value of the
     * <tt>source</tt> attribute.
     */
    private SourceConverter converter;

    /** Validator instance. */
    private Validator validator;

    /** Interpretted source instance. */
    private Source submittedSource;

    /** Intepretted report instance. */
    private JasperReport submittedReport;

    /**
     * Instantiates a new UIReport.
     */
    public UIReport() {
        super();
    }

    /**
     * Obtains the family name.
     *
     * @return the family name.
     */
    public final String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    /**
     * Obtains the established report source.
     *
     * @return the report source.
     */
    public final Object getSource() {
        if (source != null) {
            return source;
        }
        final ValueExpression ve = getValueExpression("source");
        if (ve != null) {
            try {
                return ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return source;
        }
    }

    /**
     * Establishes a new report source.
     *
     * @param source the new report source.
     */
    public final void setSource(final Object source) {
        this.source = source;
    }

    /**
     * Obtains the name of the report.
     *
     * @return the name of the report
     */
    public final String getName() {
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

    /**
     * Establishes a new name for the report.
     *
     * @param name the new report name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Obtains the current report value.
     *
     * @return the report value
     */
    public final Object getValue() {
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

    /**
     * Establishes a new value for this project.
     *
     * @param value the new value.
     */
    public final void setValue(final Object value) {
        this.value = value;
        this.valueSet = true;
    }

    /**
     * Used to identify if current value has been programatically set.
     *
     * @return true if report's value has been set by means of the
     *         <tt>setValue</tt> method, false otherwise.
     */
    public final boolean isLocalValueSet() {
        return valueSet;
    }

    /**
     * Obtains the current converter instance.
     *
     * @return a source converter instance.
     */
    public final SourceConverter getConverter() {
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

    /**
     * Establishes a new source converter instance.
     *
     * @param converter a new converter instance.
     */
    public final void setConverter(final SourceConverter converter) {
        this.converter = converter;
    }

    /**
     * Obtain the report validator instance.
     *
     * @return the validator instance.
     */
    public final Validator getValidator() {
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

    /**
     * Establishes a new validator instance.
     *
     * @param validator the validator instance.
     */
    public final void setValidator(final Validator validator) {
        this.validator = validator;
    }

    /**
     * Obtains the interpretted report source.
     *
     * @return the interpretted report source.
     */
    public final Source getSubmittedSource() {
        return submittedSource;
    }

    /**
     * Establishes the value for the report source.
     *
     * @param submittedSource the report source.
     */
    public final void setSubmittedSource(final Source submittedSource) {
        this.submittedSource = submittedSource;
    }

    /**
     * Obtains the interpretted report value.
     *
     * @return the interpretted report value.
     */
    public final JasperReport getSubmittedReport() {
        return submittedReport;
    }

    /**
     * Establishes the interpretted report value.
     *
     * @param submittedReport the report value.
     */
    public final void setSubmittedReport(final JasperReport submittedReport) {
        this.submittedReport = submittedReport;
    }

    /**
     * Obtains the valid flag.
     *
     * @return true is this report is well configured.
     */
    public final boolean isValid() {
        return valid;
    }

    /**
     * Establishes the value for the valid flag.
     *
     * @param valid if this report is valid.
     */
    protected final void setValid(final boolean valid) {
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
        validator = (Validator) values[7];
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.StateHolder#saveState(javax.faces.context.
     * FacesContext
     * )
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[8];
        values[0] = super.saveState(context);
        values[1] = source;
        values[2] = name;
        values[3] = value;
        values[4] = valueSet;
        values[5] = converter;
        values[6] = valid;
        values[7] = validator;
        return values;
    }

    /**
     * Resets the report component state.
     */
    public void resetValue() {
        this.value = null;
        this.valueSet = false;
        this.valid = true;
        this.submittedSource = null;
    }

    /**
     * Performs the decoding process in which the source value is interpretted.
     *
     * @param context current faces' context.
     */
    @Override
    public void processDecodes(final FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        if (!isRendered()) {
            return;
        }

        resetValue();
        super.processDecodes(context);

        executeDecodes(context);
    }

    /**
     * Method invoked during the <tt>PROCESS_VALIDATORS</tt> phase.
     *
     * @param context current faces' instance.
     */
    @Override
    public void processValidators(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (!isRendered()) {
            return;
        }

        super.processValidators(context);
        executeValidate(context);
    }

    /**
     * Decodes the <tt>source</tt> value into a <tt>Source</tt> instace.
     *
     * @param context current faces' context.
     */
    protected final void decodeSource(final FacesContext context) {
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

    /**
     * Decodes the report value.
     *
     * @param context current faces' context.
     */
    protected final void decodeReport(final FacesContext context) {
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

    /**
     * Validates the current report instance.
     *
     * @param context current faces' context
     * @throws ValidatorException when report configuration is not valid.
     */
    public void validate(final FacesContext context)
    throws ValidatorException {
        if (context == null) {
            throw new NullPointerException();
        }

        Validator aValidator = getValidator();
        if (aValidator == null) {
            aValidator = getFacesContext().getApplication()
                .createValidator(ReportValidatorBase.VALIDATOR_TYPE);
        }

        if (aValidator != null) {
            try {
                aValidator.validate(context, this, getValue());
            } catch (ValidatorException e) {
                setValid(false);
                throw e;
            }
        }
    }

    /**
     * Utility method for obtaining the current JRFacesContext instance.
     *
     * @return current JRFacesContext instance.
     */
    protected final JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

    /**
     * Executes the decoding process.
     *
     * @param context current faces' context.
     */
    protected final void executeDecodes(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        try {
            decodeSource(context);
            decodeReport(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    /**
     * Executes the validation process.
     *
     * @param context current faces' context.
     */
    protected final void executeValidate(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        try {
            validate(context);
        } catch (ValidatorException e) {
            context.renderResponse();
            throw e;
        }
    }

}
