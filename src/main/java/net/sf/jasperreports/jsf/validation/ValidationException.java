package net.sf.jasperreports.jsf.validation;

import net.sf.jasperreports.jsf.JRFacesException;

public class ValidationException extends JRFacesException {

	public ValidationException(String msg, Throwable t) {
		super(msg, t);
	}

	public ValidationException(String msg) {
		super(msg);
	}

	public ValidationException(Throwable t) {
		super(t);
	}

}
