/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.fill.providers;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.fill.FillerException;
import net.sf.jasperreports.jsf.fill.AbstractQueryFiller;

// TODO: Auto-generated Javadoc
/**
 * The Class JdbcFiller.
 */
public class JdbcFiller extends AbstractQueryFiller {

    /** The Constant REQUIRED_DATASOURCE_ATTRS. */
    public static final String[] REQUIRED_DATASOURCE_ATTRS = {
            "username", "password"
    };

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(JdbcFiller.class
            .getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

    /* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.fill.Filler#getRequiredDataSourceAttributes()
     */
    @Override
    public String[] getRequiredDataSourceAttributes() {
        return REQUIRED_DATASOURCE_ATTRS;
    }

    /* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.fill.Filler#doFill(javax.faces.context.FacesContext, java.io.InputStream, java.util.Map)
     */
    @Override
    protected JasperPrint doFill(final FacesContext context,
            final InputStream reportStream, final Map<String, Object> params)
            throws FillerException {
        final String driverClass = getDataSourceComponent().getDriverClass();
        if ((driverClass == null) || (driverClass.length() == 0)) {
            throw new FillerException(
                    "DEFAULT dataSource type requires a driverClass value!");
        }

        try {
            Class.forName(driverClass);
        } catch (final ClassNotFoundException e) {
            throw new FillerException("Driver class not found: " + driverClass,
                    e);
        }
        logger.log(Level.FINE, "JRJSF_0004", driverClass);

        final String connectionURL = (String) getDataSourceComponent()
                .getValue();
        final String username = (String) getDataSourceComponent()
                .getAttributes().get("username");
        final String password = (String) getDataSourceComponent()
                .getAttributes().get("password");

        Connection conn = null;
        ResultSet rs = null;
        JRDataSource dataSource = null;
        try {
            if (username == null) {
                logger.log(Level.FINE, "JRJSF_0007", connectionURL);
                conn = DriverManager.getConnection(connectionURL);
            } else {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "JRJSF_0008", new Object[] {
                            connectionURL, username
                    });
                }
                conn = DriverManager.getConnection(connectionURL, username,
                        password);
            }
        } catch (final SQLException e) {
            throw new FillerException(e);
        }

        final String query = getDataSourceComponent().getQuery();
        if ((query != null) && (query.length() > 0)) {
            rs = executeQuery(conn);
            dataSource = new JRResultSetDataSource(rs);
        }

        JasperPrint result = null;
        try {
            if (dataSource == null) {
                result = JasperFillManager.fillReport(reportStream, params,
                        conn);
            } else {
                result = JasperFillManager.fillReport(reportStream, params,
                        dataSource);
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        }
        return result;
    }

}
