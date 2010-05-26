/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.spi;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.spi.Services;

/**
 *
 * @author antonio.alonso
 */
public abstract class JRDataSourceLoader {

    private static final JRDataSourceLoader DEFAULT =
            new DefaultJRDataSourceLoader();

    public static JRDataSourceLoader getInstance() {
        return Services.chain(JRDataSourceLoader.class, DEFAULT);
    }

    public abstract JRDataSource loadDataSource(FacesContext context,
            UIDataSource component);

}
