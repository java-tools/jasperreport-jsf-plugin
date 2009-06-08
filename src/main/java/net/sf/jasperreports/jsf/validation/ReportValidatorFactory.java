package net.sf.jasperreports.jsf.validation;

import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.util.Util;

public final class ReportValidatorFactory {

	private static final Map<String, Class<ReportValidator>> validatorCacheMap =
		Util.loadServiceMap(ReportValidator.class);
	
	public static ReportValidator getValidator(FacesContext context, UIReport report) 
	throws ValidationException {
		ReportValidator result = null;
		Class<ReportValidator> validatorClass = validatorCacheMap.get(report.getFormat());
		if(validatorClass == null) {
			validatorClass = validatorCacheMap.get(null);
		}
		if(validatorClass != null) {
			try {
				result = validatorClass.newInstance();
			} catch(Exception e) {
				throw new ValidationException(e);
			}
		}
		return result;
	}
	
	private ReportValidatorFactory() { }
	
}
