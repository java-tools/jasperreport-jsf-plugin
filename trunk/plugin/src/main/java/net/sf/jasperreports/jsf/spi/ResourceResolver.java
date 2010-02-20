/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.spi;

import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.resource.Resource;

/**
 *
 * @author aalonsodominguez
 */
public interface ResourceResolver {

    public Resource resolveResource(FacesContext context, String name);
    
}
