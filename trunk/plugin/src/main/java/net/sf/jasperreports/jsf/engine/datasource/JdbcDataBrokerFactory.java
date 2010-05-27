/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataBroker;

/**
 *
 * @author aalonsodominguez
 */
public class JdbcDataBrokerFactory extends DatabaseDataBrokerFactory {

    public static final String ATTR_DRIVER_CLASS_NAME = "driverClassName";
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_PASSWORD = "password";

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(
            JdbcDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    @Override
    protected Connection getConnection(FacesContext context, UIDataBroker component) {
        final String driverClass = (String) component
                .getAttributes().get(ATTR_DRIVER_CLASS_NAME);
        if ((driverClass == null) || (driverClass.length() == 0)) {
            throw new JRFacesException(
                    "DEFAULT dataSource type requires a driverClassName value!");
        }

        try {
            Class.forName(driverClass);
        } catch (final ClassNotFoundException e) {
            throw new JRFacesException("Driver class not found: " + driverClass,
                    e);
        }
        logger.log(Level.FINE, "JRJSF_0004", driverClass);

        final String connectionURL = (String) component.getData();
        final String username = (String) component
                .getAttributes().get(ATTR_USERNAME);
        final String password = (String) component
                .getAttributes().get(ATTR_PASSWORD);

        if (connectionURL == null || connectionURL.length() == 0) {
            throw new JRFacesException(
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
            throw new JRFacesException(e);
        }
        return conn;
    }

}
