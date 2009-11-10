package net.sf.jasperreports.jsf.resource;

import net.sf.jasperreports.jsf.JRFacesException;

public class ResourceException extends JRFacesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3926030327829973068L;

	public ResourceException(final String msg, final Throwable t) {
		super(msg, t);
	}

	public ResourceException(final String msg) {
		super(msg);
	}

	public ResourceException(final Throwable t) {
		super(t);
	}

}
