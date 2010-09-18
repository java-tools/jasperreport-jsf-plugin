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
package net.sf.jasperreports.jsf.test.mock;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author aalonsodominguez
 */
public class MockJdbcDriver implements Driver {

    public Connection connect(String url, Properties info) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean acceptsURL(String url) throws SQLException {
        if (url == null) return false;
        return (url.startsWith("jdbc:mock"));
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean jdbcCompliant() {
        return true;
    }

}
