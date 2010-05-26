/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.datasource;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.spi.JRDataSourceFactory;

/**
 *
 * @author antonio.alonso
 */
public class BeanDataSourceFactory implements JRDataSourceFactory {

    private static final Logger logger = Logger.getLogger(
            BeanDataSourceFactory.class.getPackage().getName(),
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
            dataSource = new JRBeanCollectionDataSource((Collection<?>) value);
        } else {
            Object[] beanArray;
            if (!value.getClass().isArray()) {
                beanArray = new Object[]{ value };
            } else {
                beanArray = (Object[]) value;
            }
            dataSource = new JRBeanArrayDataSource(beanArray);
        }
        return dataSource;
    }

}
