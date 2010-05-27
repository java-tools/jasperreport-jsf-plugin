/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.spi;

import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;

/**
 *
 * @author antonio.alonso
 */
public interface JRDataSourceFactory {

    public JRDataSource createDataSource(FacesContext context,
            UIDataSource component);

    public void dispose(FacesContext context, JRDataSource dataSource);

}
