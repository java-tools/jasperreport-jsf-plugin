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
package net.sf.jasperreports.jsf.fill.providers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractSQLFiller;
import net.sf.jasperreports.jsf.fill.FillerException;

/**
 * The Class JndiFiller.
 */
public final class JndiFiller extends AbstractSQLFiller {

    protected JndiFiller(final UIDataSource dataSource) {
        super(dataSource);
    }
    
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(
            JndiFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.fill.AbstractQueryFiller#getConnection()
     */
    @Override
    protected Connection getConnection() throws FillerException {
        final String dataSourceName = (String) getDataSourceComponent().getValue();
        if (dataSourceName == null || dataSourceName.length() == 0) {
            throw new FillerException("JNDI Filler requires a JNDI name"
                    + " from a dataSource component");
        }
        logger.log(Level.FINE, "JRJSF_0005", dataSourceName);

        try {
            final Context jndi = new InitialContext();
            final DataSource ds = (DataSource) jndi.lookup(dataSourceName);
            return ds.getConnection();
        } catch(NamingException e) {
            throw new FillerException(e);
        } catch(SQLException e) {
            throw new FillerException(e);
        }
    }
}
