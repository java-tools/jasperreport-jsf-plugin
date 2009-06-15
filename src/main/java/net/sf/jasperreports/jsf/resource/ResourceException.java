package net.sf.jasperreports.jsf.resource;

import net.sf.jasperreports.jsf.JRFacesException;

public class ResourceException extends JRFacesException {

	public ResourceException(String msg, Throwable t) {
		super(msg, t);
	}

	public ResourceException(String msg) {
		super(msg);
	}

	public ResourceException(Throwable t) {
		super(t);
	}

}
