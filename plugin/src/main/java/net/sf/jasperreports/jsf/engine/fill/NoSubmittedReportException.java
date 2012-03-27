/*
 * JaspertReports JSF Plugin Copyright (C) 2012 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.engine.fill;

import net.sf.jasperreports.jsf.engine.FillerException;

/**
 * Exception thrown when a <tt>Filler</tt> implementation can't
 * find the report resource.
 *
 * @author A. Alonso Dominguez
 */
public class NoSubmittedReportException extends FillerException {

    /**
     *
     */
    private static final long serialVersionUID = 3585888152288225298L;

    /**
     * Instantiates a new report not found exception.
     *
     * @param msg the message
     */
    public NoSubmittedReportException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new report not found exception.
     *
     * @param msg the message
     * @param t the cause
     */
    public NoSubmittedReportException(final String msg, final Throwable t) {
        super(msg, t);
    }
}
