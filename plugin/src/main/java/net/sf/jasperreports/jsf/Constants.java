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
package net.sf.jasperreports.jsf;

import net.sf.jasperreports.jsf.component.UIReport;

/**
 *
 * @author A. Alonso Dominguez
 */
public final class Constants {

    public static final String PACKAGE_PREFIX =
		Constants.class.getPackage().getName();

    public static final String ATTR_REPORT_VIEW =
            PACKAGE_PREFIX + ".REPORT_VIEW";

    public static final String ATTR_POSTBACK =
            PACKAGE_PREFIX + ".POSTBACK";

    public static final String ATTR_VIEW_STATE =
            PACKAGE_PREFIX + ".VIEW_STATE";

    /** Base string URI used to recognize Report render requests. */
    public static final String BASE_URI = "/" + PACKAGE_PREFIX + "/render";

    /** Parameter identifying the component which sended the request. */
    public static final String PARAM_CLIENTID = 
            PACKAGE_PREFIX + ".clientId";

    public static final String PARAM_VIEWID =
            PACKAGE_PREFIX + ".viewId";

    /** Internal key prefix used to obtain the report component. */
    public static final String REPORT_COMPONENT_KEY_PREFIX =
            UIReport.COMPONENT_FAMILY + "/";

    public static final String VIEW_CACHE_KEY =
            PACKAGE_PREFIX + ".VIEW_CACHE";

    /** Private constructor to prevent instantiation. */
    private Constants() { }

}