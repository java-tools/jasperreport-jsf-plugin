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
package net.sf.jasperreports.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.export.Exporter;
import net.sf.jasperreports.jsf.fill.Filler;
import net.sf.jasperreports.jsf.spi.ExporterLoader;
import net.sf.jasperreports.jsf.spi.FillerLoader;
import net.sf.jasperreports.jsf.util.Util;

/**
 * The listener interface for receiving reportPhase events. The class that is
 * interested in processing a reportPhase event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addReportPhaseListener</code> method. When the reportPhase
 * event occurs, that object's appropriate method is invoked.
 * 
 * @see ReportPhaseEvent
 */
public final class ReportPhaseListener implements PhaseListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -124696216613450702L;

	/** The Constant BASE_URI. */
	public static final String BASE_URI = "/___jasperreportsjsf";

	/** The Constant PARAM_CLIENTID. */
	public static final String PARAM_CLIENTID = "clientId";

	/** The Constant REPORT_COMPONENT_KEY_PREFIX. */
	public static final String REPORT_COMPONENT_KEY_PREFIX = UIReport.COMPONENT_FAMILY
			+ "/";

	/** The logger. */
	private static final Logger logger = Logger.getLogger(
			ReportPhaseListener.class.getPackage().getName(),
			"net.sf.jasperreports.jsf.LogMessages");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
	public void afterPhase(final PhaseEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
	public void beforePhase(final PhaseEvent event) throws FacesException {
		final FacesContext context = event.getFacesContext();
		final String uri = Util.getInstance(context).getRequestURI(context);
		if (uri != null && uri.indexOf(BASE_URI) > -1) {
			final ExternalContext extContext = context.getExternalContext();

			final String clientId = context.getExternalContext()
					.getRequestParameterMap().get(PARAM_CLIENTID);
			if (clientId == null) {
				throw new MalformedReportURLException("Missed parameter: "
						+ PARAM_CLIENTID);
			}

			final UIReport report = (UIReport) extContext.getSessionMap()
					.remove(REPORT_COMPONENT_KEY_PREFIX + clientId);
			if (report == null) {
				throw new UnregisteredUIReportException(clientId);
			}

			final Filler filler = FillerLoader.getFiller(context, report);
			logger.log(Level.FINE, "JRJSF_0006", clientId);
			final JasperPrint filledReport = filler.fill(context, report);

			final Exporter exporter = ExporterLoader.getExporter(context,
					report);
			final ByteArrayOutputStream reportData = new ByteArrayOutputStream();
			try {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "JRJSF_0010", new Object[] {
							clientId, exporter.getContentType() });
				}
				exporter.export(context, filledReport, reportData);
				Util.getInstance(context).writeResponse(context, exporter.getContentType(),
						reportData.toByteArray());
			} catch (final IOException e) {
				throw new JRFacesException(e);
			} finally {
				try {
					reportData.close();
				} catch (final IOException e) {
					// ignore
				}
				context.responseComplete();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#getPhaseId()
	 */
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
