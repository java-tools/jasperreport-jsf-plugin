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

import java.sql.Connection;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRDataSource;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.engine.ReportSourceFactory;
import net.sf.jasperreports.jsf.engine.source.JRDataSourceHolder;
import net.sf.jasperreports.jsf.engine.source.ConnectionHolder;
import net.sf.jasperreports.jsf.validation.MissingAttributeException;
import net.sf.jasperreports.jsf.validation.ValidationException;
import net.sf.jasperreports.jsf.validation.Validator;

/**
 * The Class UIDataSource.
 */
public class UIReportSource extends UIComponentBase {

    /** The Constant COMPONENT_FAMILY. */
    public static final String COMPONENT_FAMILY =
            Constants.PACKAGE_PREFIX + ".DataBroker";
    
    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".DataBroker";

    // Fields

    /** The query. */
    private String query = null;
    /** The type. */
    private String type = null;
    /** The type set. */
    private boolean typeSet = false;
    /** The data. */
    private Object data = null;
    private boolean valid = true;
    /** The value */
    private Object value;
    private boolean valueSet = false;

    private ReportSourceFactory factory;
    private Validator validator;

    /**
     * Instantiates a new uI data source.
     */
    public UIReportSource() {
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
                return (String) ve.getValue(getFacesContext().getELContext());
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

    /**
     * Gets the data.
     *
     * @return the data
     */
    public final Object getData() {
        if (data != null) {
            return data;
        }
        final ValueExpression ve = getValueExpression("data");
        if (ve != null) {
            try {
                return ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return data;
        }
    }

    /**
     * Sets the data.
     *
     * @param data the new value
     */
    public final void setData(final Object data) {
        this.data = data;
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
                return ve.getValue(getFacesContext().getELContext());
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

    public ReportSourceFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        ValueExpression ve = getValueExpression("factory");
        if (ve != null) {
            try {
                return (ReportSourceFactory) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return factory;
        }
    }

    public void setFactory(ReportSourceFactory factory) {
        this.factory = factory;
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
        data = values[4];
        factory = (ReportSourceFactory) values[5];
        validator = (Validator) values[6];
        valid = ((Boolean) values[7]).booleanValue();
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
        values[4] = data;
        values[5] = factory;
        values[6] = validator;
        values[7] = valid;
        return values;
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
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
            throw new NullPointerException();
        }

        super.processValidators(context);
        executeValidate(context);
    }

    public void updateModel(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (!isValid() || valueSet) {
            return;
        }

        // Obtain a DataBroker instance
        ReportSource dataBroker;

        boolean sendInRequest = true;
        Object providedValue = getValue();
        if (providedValue == null) {
            ReportSourceFactory factory = this.getFactory();
            if (factory != null) {
                dataBroker = factory.createSource(context, this);
            } else {
                dataBroker = getJRFacesContext().createDataSource(context, this);
            }
            assert dataBroker != null;

            // Now assign the data broker to the model bean (if applicable)
            ValueExpression ve = getValueExpression("value");
            if (ve != null) {
                sendInRequest = false;
                resetValue();
                try {
                    ve.setValue(context.getELContext(), dataBroker);
                } catch (ELException e) {
                    setValid(false);
                    throw new FacesException(e);
                }
            }
        } else {
            dataBroker = createDataBroker(providedValue);
            if (dataBroker == null) {
                setValid(false);
                throw new JRFacesException("Unsupported data broker type: "
                        + value.getClass());
            }
        }

        if (sendInRequest) {
            String clientId = getClientId(context);
            context.getExternalContext().getRequestMap()
                    .put(clientId, dataBroker);
        }

    }

    public void validate(FacesContext context) throws ValidationException {
        if (context == null) {
            throw new NullPointerException();
        }

        Object providedValue = getValue();
        if (providedValue == null) {
            String providedType = getType();
            if (providedType == null || providedType.length() == 0) {
                setValid(false);
                throw new MissingAttributeException("type");
            }
            Object providedData = getData();
            if (providedData == null) {
                setValid(false);
                throw new MissingAttributeException("data");
            }

            Validator validator = getValidator();
            if (validator == null) {
                validator = getJRFacesContext()
                        .createValidator(context, this);
            }

            if (validator != null) {
                try {
                    validator.validate(context, this);
                } catch(ValidationException e) {
                    setValid(false);
                    throw e;
                }
            }
        }
    }

    protected ReportSource createDataBroker(Object value) {
        if(value instanceof ReportSource) {
            return (ReportSource) value;
        } else if (value instanceof Connection) {
            return new ConnectionHolder((Connection) value);
        } else if (value instanceof JRDataSource) {
            return new JRDataSourceHolder((JRDataSource) value);
        } else {
            return null;
        }
    }

    protected void executeValidate(FacesContext context)
            throws ValidationException {
        Object providedValue = getValue();
        if (providedValue == null) {
            try {
                validate(context);
            } catch(ValidationException e) {
                context.renderResponse();
                throw e;
            }
        }
    }

    protected JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

}
