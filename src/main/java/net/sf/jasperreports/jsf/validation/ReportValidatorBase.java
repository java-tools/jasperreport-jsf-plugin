package net.sf.jasperreports.jsf.validation;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.export.ExporterFactory;

public class ReportValidatorBase implements ReportValidator {

	public void validate(FacesContext context, UIReport report)
			throws ValidationException {
		if(!ExporterFactory.getAvailableExportFormats()
				.contains(report.getFormat())) {
			throw new IllegalOutputFormatException(report.getFormat());
		}
	}

}
