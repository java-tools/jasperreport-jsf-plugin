package net.sf.jasperreports.jsf.validation;

import net.sf.jasperreports.jsf.JRFacesException;

public class ValidationException extends JRFacesException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8248094331118863059L;

	public ValidationException(final String msg, final Throwable t) {
		super(msg, t);
	}

	public ValidationException(final String msg) {
		super(msg);
	}

	public ValidationException(final Throwable t) {
		super(t);
	}

}
