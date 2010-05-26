/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.spi;

import java.util.Map;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.spi.Services;

/**
 *
 * @author antonio.alonso
 */
public class DefaultJRDataSourceLoader extends JRDataSourceLoader {

    private static Map<String, JRDataSourceFactory> serviceMap =
            Services.map(JRDataSourceFactory.class);

    @Override
    public JRDataSource loadDataSource(FacesContext context,
            UIDataSource component) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (component == null) {
            throw new IllegalArgumentException();
        }

        JRDataSource result = null;
        JRDataSourceFactory factory = serviceMap.get(component.getType());
        if (factory != null) {
            result = factory.createDataSource(context, component);
        } else {
            result = new JREmptyDataSource();
        }
        return result;
    }

}
