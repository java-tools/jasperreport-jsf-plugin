/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.validation;

/**
 * The Class MissedDataSourceAttributeException.
 */
public class MissedAttributeException extends ValidationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6313893570302881706L;

    /**
     * Instantiates a new missed data source attribute exception.
     *
     * @param msg
     *            the msg
     */
    public MissedAttributeException(final String msg) {
        super(msg);
    }
}
