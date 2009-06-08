package net.sf.jasperreports.jsf.validation.provider;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.validation.DataSourceValidator;
import net.sf.jasperreports.jsf.validation.MissedDataSourceAttributeException;
import net.sf.jasperreports.jsf.validation.ValidationException;

public class JdbcDataSourceValidator implements DataSourceValidator {

	/** The Constant REQUIRED_DATASOURCE_ATTRS. */
    public static final String[] REQUIRED_DATASOURCE_ATTRS = {
            "username", "password"
    };
	
	public void validate(FacesContext context, UIDataSource dataSource)
			throws ValidationException {
		for (final String attr : REQUIRED_DATASOURCE_ATTRS) {
            if (null == dataSource.getAttributes().get(attr)) {
                throw new MissedDataSourceAttributeException(attr);
            }
        }
	}

}
