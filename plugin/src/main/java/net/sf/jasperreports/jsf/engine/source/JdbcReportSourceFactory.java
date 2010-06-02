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
package net.sf.jasperreports.jsf.engine.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.engine.ReportSourceException;

/**
 *
 * @author aalonsodominguez
 */
public class JdbcReportSourceFactory extends DatabaseReportSourceFactory {

    public static final String ATTR_DRIVER_CLASS_NAME = "driverClassName";
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_PASSWORD = "password";

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(
            JdbcReportSourceFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    @Override
    protected Connection getConnection(
            FacesContext context, UIReportSource component)
    throws ReportSourceException {
        final String driverClass = (String) component
                .getAttributes().get(ATTR_DRIVER_CLASS_NAME);
        if ((driverClass == null) || (driverClass.length() == 0)) {
            throw new ReportSourceException(
                    "jdbc report source type requires a driverClassName value!");
        }

        try {
            Class.forName(driverClass);
        } catch (final ClassNotFoundException e) {
            throw new ReportSourceException("Driver class not found: " +
                    driverClass, e);
        }
        logger.log(Level.FINE, "JRJSF_0004", driverClass);

        final String connectionURL = (String) component.getData();
        final String username = (String) component
                .getAttributes().get(ATTR_USERNAME);
        final String password = (String) component
                .getAttributes().get(ATTR_PASSWORD);

        if (connectionURL == null || connectionURL.length() == 0) {
            throw new ReportSourceException(
                    "JDBC Filler requires a connection string"
                    + " from a dataSource component");
        }

        Connection conn = null;
        try {
            if (username == null) {
                logger.log(Level.FINE, "JRJSF_0007", connectionURL);
                conn = DriverManager.getConnection(connectionURL);
            } else {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "JRJSF_0008", new Object[]{
                                connectionURL, username});
                }
                conn = DriverManager.getConnection(connectionURL, username,
                        password);
            }
        } catch (final SQLException e) {
            throw new ReportSourceException(e);
        }
        return conn;
    }

}
