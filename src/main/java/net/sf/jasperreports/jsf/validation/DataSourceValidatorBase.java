package net.sf.jasperreports.jsf.validation;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.FillerLoader;

public class DataSourceValidatorBase implements DataSourceValidator {

	public void validate(FacesContext context, UIDataSource dataSource)
			throws ValidationException {
		if(!FillerLoader.getAvailableDataSourceTypes()
				.contains(dataSource.getType())) {
			throw new IllegalDataSourceTypeException(dataSource.getType());
		}
	}

}
