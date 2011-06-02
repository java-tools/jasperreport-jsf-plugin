package net.sf.jasperreports.jsf.context;

import net.sf.jasperreports.jsf.util.ServiceException;

public class UnsupportedContentTypeException extends ServiceException {

	private ContentType contentType;
	
	public UnsupportedContentTypeException(ContentType contentType) {
		super(contentType.toString());
		this.contentType = contentType;
	}

	public ContentType getContentType() {
		return contentType;
	}

}
