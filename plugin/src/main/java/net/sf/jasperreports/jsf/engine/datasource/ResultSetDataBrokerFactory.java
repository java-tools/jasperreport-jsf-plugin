/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.datasource;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.jsf.component.UIDataBroker;

/**
 *
 * @author aalonsodominguez
 */
public class ResultSetDataBrokerFactory extends AbstractDataBrokerFactory {

    private static final Logger logger = Logger.getLogger(
            ResultSetDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public DataBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        JRDataSource dataSource;
        final ResultSet rs = (ResultSet) component.getData();
        if (rs == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else {
            dataSource = new JRResultSetDataSource(rs);
        }
        return new JRDataSourceBroker(dataSource);
    }

}
