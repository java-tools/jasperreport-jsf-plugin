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
package net.sf.jasperreports.jsf.engine;

import net.sf.jasperreports.jsf.JRFacesException;

/**
 * Root class for filler exceptions.
 *
 * @author A. Alonso Dominguez
 */
public class FillerException extends JRFacesException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3240029115273719789L;

    /**
     * Instantiates a new filler exception.
     *
     * @param msg the message.
     * @param t the cause.
     */
    public FillerException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new filler exception.
     *
     * @param msg the message.
     */
    public FillerException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new filler exception.
     *
     * @param t the cause.
     */
    public FillerException(final Throwable t) {
        super(t);
    }
}
