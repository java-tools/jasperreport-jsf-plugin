package net.sf.jasperreports.jsf.renderkit;

import net.sf.jasperreports.jsf.JRFacesException;

public final class UnsupportedExporterException extends JRFacesException {

	public UnsupportedExporterException(String exporterType) {
		super(exporterType);
	}
	
}
