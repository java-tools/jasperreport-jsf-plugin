/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.export.providers;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractExporter;

/**
 * The Class TextExporter.
 */
public final class TextExporter extends AbstractExporter {

	public static final String CONTENT_TYPE = "text/plain";

	/** The Constant ATTR_BETWEEN_PAGES_TEXT. */
	public static final String ATTR_BETWEEN_PAGES_TEXT = "BETWEEN_PAGES_TEXT";

	/** The Constant ATTR_CHARACTER_HEIGHT. */
	public static final String ATTR_CHARACTER_HEIGHT = "CHARACTER_HEIGHT";

	/** The Constant ATTR_CHARACTER_WIDTH. */
	public static final String ATTR_CHARACTER_WIDTH = "CHARACTER_WIDTH";

	/** The Constant ATTR_LINE_SEPARATOR. */
	public static final String ATTR_LINE_SEPARATOR = "LINE_SEPARATOR";

	/** The Constant ATTR_PAGE_HEIGHT. */
	public static final String ATTR_PAGE_HEIGHT = "PAGE_HEIGHT";

	/** The Constant ATTR_PAGE_WIDTH. */
	public static final String ATTR_PAGE_WIDTH = "PAGE_WIDTH";

	protected TextExporter(final UIComponent component) {
		super(component);
	}

	public String getContentType() {
		return CONTENT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jasperreports.jsf.export.Exporter#createJRExporter(javax.faces
	 * .context.FacesContext)
	 */
	@Override
	protected JRExporter createJRExporter(final FacesContext context) {
		final JRTextExporter exporter = new JRTextExporter();
		exporter.setParameter(JRTextExporterParameter.BETWEEN_PAGES_TEXT,
				getComponent().getAttributes().get(ATTR_BETWEEN_PAGES_TEXT));
		exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT,
				getComponent().getAttributes().get(ATTR_CHARACTER_HEIGHT));
		exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH,
				getComponent().getAttributes().get(ATTR_CHARACTER_WIDTH));
		exporter.setParameter(JRTextExporterParameter.LINE_SEPARATOR,
				getComponent().getAttributes().get(ATTR_LINE_SEPARATOR));
		exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT,
				getComponent().getAttributes().get(ATTR_PAGE_HEIGHT));
		exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH,
				getComponent().getAttributes().get(ATTR_PAGE_WIDTH));
		return exporter;
	}

}
