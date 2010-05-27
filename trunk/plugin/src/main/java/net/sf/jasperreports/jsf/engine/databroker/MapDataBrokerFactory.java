/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.databroker;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.component.UIDataBroker;

/**
 *
 * @author antonio.alonso
 */
public class MapDataBrokerFactory extends AbstractDataBrokerFactory {

    private static final Logger logger = Logger.getLogger(
            MapDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public DataBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        JRDataSource dataSource;

        final Object value = component.getData();
        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else if (value instanceof Collection<?>) {
            dataSource = new JRMapCollectionDataSource((Collection<?>) value);
        } else {
            Map<?, ?>[] mapArray;
            if (!value.getClass().isArray()) {
                mapArray = new Map[]{(Map<?, ?>) value};
            } else {
                mapArray = (Map[]) value;
            }
            dataSource = new JRMapArrayDataSource(mapArray);
        }
        return new JRDataSourceBroker(dataSource);
    }

}
