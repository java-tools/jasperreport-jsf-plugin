package net.sf.jasperreports.jsf.spi;

public class ResourceFactoryNotFoundException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5741641651418076357L;

	public ResourceFactoryNotFoundException(final String msg, final Throwable t) {
		super(msg, t);
	}

	public ResourceFactoryNotFoundException(final String msg) {
		super(msg);
	}

}
