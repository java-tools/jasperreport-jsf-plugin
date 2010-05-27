/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.datasource;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.spi.JRDataSourceFactory;

/**
 *
 * @author antonio.alonso
 */
public class MapDataSourceFactory implements JRDataSourceFactory {

    private static final Logger logger = Logger.getLogger(
            MapDataSourceFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public JRDataSource createDataSource(FacesContext context,
            UIDataSource component) {
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
        return dataSource;
    }

    public void dispose(FacesContext context, JRDataSource dataSource) { }

}
