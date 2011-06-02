package net.sf.jasperreports.jsf.renderkit;

import net.sf.jasperreports.jsf.JRFacesException;

public final class UnsupportedExporterException extends JRFacesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1481743856063619890L;

	public UnsupportedExporterException(String exporterType) {
		super(exporterType);
	}
	
}
