/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.datasource;

import java.sql.Connection;

/**
 *
 * @author aalonsodominguez
 */
public class SqlConnectionBroker implements DataBroker {

    private Connection connection;

    public SqlConnectionBroker(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

}
