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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.validation.SourceValidatorBase;

/**
 * The Class UIDataSource.
 */
public class UISource extends UIComponentBase {

    /** The Constant COMPONENT_FAMILY. */
    public static final String COMPONENT_FAMILY =
            Constants.PACKAGE_PREFIX + ".Source";
    
    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".Source";

    // Fields

    /** The query. */
    private String query = null;
    /** The type. */
    private String type = null;
    /** The type set. */
    private boolean typeSet = false;
    private boolean valid = true;
    /** The value */
    private Object value;
    private boolean valueSet = false;

    private SourceConverter converter;
    private Validator validator;

    private Source submittedSource;

    /**
     * Instantiates a new uI data source.
     */
    public UISource() {
        super();
        setRendererType(null);
    }

    // Properties

    /**
     * Gets the query.
     *
     * @return the query
     */
    public final String getQuery() {
        if (query != null) {
            return query;
        }
        final ValueExpression ve = getValueExpression("query");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return query;
        }
    }

    /**
     * Sets the query.
     *
     * @param query the new query
     */
    public final void setQuery(final String query) {
        this.query = query;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public final String getType() {
        if (typeSet) {
            return type;
        }
        final ValueExpression ve = getValueExpression("type");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return type;
        }
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public final void setType(final String type) {
        this.type = type;
        typeSet = true;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public final Object getValue() {
        if (valueSet) {
            return value;
        }
        final ValueExpression ve = getValueExpression("value");
        if (ve != null) {
            try {
                return ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return value;
        }
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public final void setValue(final Object value) {
        this.value = value;
        valueSet = true;
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
            } catch(ELException e) {
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

    // UIComponent

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void resetValue() {
        setValue(null);
        setValid(true);
        setSubmittedSource(null);
        valueSet = false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.UIComponentBase#restoreState(javax.faces.context
     * .FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        query = (String) values[1];
        type = (String) values[2];
        typeSet = ((Boolean) values[3]).booleanValue();
        converter = (SourceConverter) values[4];
        validator = (Validator) values[5];
        valid = ((Boolean) values[6]).booleanValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#saveState(javax.faces.context.
     * FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[8];
        values[0] = super.saveState(context);
        values[1] = query;
        values[2] = type;
        values[3] = Boolean.valueOf(typeSet);
        values[4] = converter;
        values[5] = validator;
        values[6] = valid;
        return values;
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        resetValue();
        super.processDecodes(context);

        try {
            validate(context);
            decodeValue(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        
        super.processUpdates(context);

        try {
            updateModel(context);
        } catch(RuntimeException e) {
            context.renderResponse();
            throw e;
        }

        if (!isValid()) {
            context.renderResponse();
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        super.processValidators(context);
        executeValidate(context);
    }

    public void decodeValue(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        SourceConverter aConverter = getConverter();
        if (aConverter == null) {
            aConverter = getJRFacesContext()
                    .createSourceConverter(context, this);
        }

        Source source = aConverter.convertFromValue(
                context, this, getValue());
        setSubmittedSource(source);
    }

    public void updateModel(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        if (!isValid() || valueSet) {
            return;
        }

        // Send report source as a request attribute
        String clientId = getClientId(context);
        context.getExternalContext().getRequestMap()
                .put(clientId, submittedSource);
    }

    public void validate(FacesContext context) throws ValidatorException {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        Validator aValidator = getValidator();
        if (aValidator == null) {
            aValidator = getFacesContext().getApplication()
                    .createValidator(SourceValidatorBase.VALIDATOR_TYPE);
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

    protected void executeValidate(FacesContext context)
            throws ValidatorException {
        Object providedValue = getValue();
        if (providedValue == null) {
            try {
                validate(context);
            } catch(ValidatorException e) {
                context.renderResponse();
                throw e;
            }
        }
    }

    protected JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

}
