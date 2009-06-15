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
package net.sf.jasperreports.jsf.fill.providers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractSQLFiller;
import net.sf.jasperreports.jsf.fill.FillerException;

/**
 * The Class JdbcFiller.
 */
public class JdbcFiller extends AbstractSQLFiller {

	protected JdbcFiller(final UIDataSource dataSource) {
		super(dataSource);
	}

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(JdbcFiller.class
			.getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.jsf.fill.AbstractQueryFiller#getConnection()
	 */
	@Override
	protected Connection getConnection() throws SQLException {
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
		try {
			if (username == null) {
				logger.log(Level.FINE, "JRJSF_0007", connectionURL);
				conn = DriverManager.getConnection(connectionURL);
			} else {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "JRJSF_0008", new Object[] {
							connectionURL, username });
				}
				conn = DriverManager.getConnection(connectionURL, username,
						password);
			}
		} catch (final SQLException e) {
			throw new FillerException(e);
		}
		return conn;
	}

}
