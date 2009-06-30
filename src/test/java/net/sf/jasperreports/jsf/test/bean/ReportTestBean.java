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
package net.sf.jasperreports.jsf.test.bean;

import net.sf.jasperreports.jsf.component.UIReport;

/**
 * The Class ReportTestBean.
 */
public class ReportTestBean {

	/** The factura id. */
	private Long facturaId;

	/** The report. */
	private UIReport report;

	/**
	 * Gets the factura id.
	 * 
	 * @return the factura id
	 */
	public Long getFacturaId() {
		return facturaId;
	}

	/**
	 * Sets the factura id.
	 * 
	 * @param facturaId
	 *            the new factura id
	 */
	public void setFacturaId(final Long facturaId) {
		this.facturaId = facturaId;
	}

	/**
	 * Gets the report.
	 * 
	 * @return the report
	 */
	public UIReport getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 * 
	 * @param report
	 *            the new report
	 */
	public void setReport(final UIReport report) {
		this.report = report;
	}

}