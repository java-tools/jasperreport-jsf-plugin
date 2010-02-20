/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jasperreports.jsf.resource.providers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceResolver;
import net.sf.jasperreports.jsf.util.ExternalContextHelper;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author aalonsodominguez
 */
public class DefaultResourceResolver implements ResourceResolver {

    public Resource resolveResource(FacesContext context, String name) {
        try {
            final URL url = new URL(name);
            return new URLResource(name, url);
        } catch (MalformedURLException e) {
            final ClassLoader loader = Util.getClassLoader(this);
            if (name.startsWith(ClasspathResource.PREFIX)) {
                return new ClasspathResource(
                        name.substring(ClasspathResource.PREFIX.length()),
                        loader);
            } else if (loader.getResourceAsStream(name) != null) {
                return new ClasspathResource(name, loader);
            } else {
                return resolveContextResource(context, name);
            }
        }
    }

    protected Resource resolveContextResource(FacesContext context, String name) {
        ExternalContextHelper helper = ExternalContextHelper.getInstance(
                context.getExternalContext());
        if (name.startsWith("/")) {
            return new ContextResource(name, context.getExternalContext());
        } else {
            String rootPath = resolveCurrentPath(context);
            File resourceFile = new File(helper.getResourceRealPath(
                    context.getExternalContext(), rootPath), name);
            if(resourceFile.exists()) {
                String absName = rootPath + name;
                return new ContextResource(absName, context.getExternalContext());
            }
        }
        return null;
    }

    protected String resolveCurrentPath(FacesContext context) {
        // TODO implement mechanism to find the current invokation path
        return null;
    }

    private boolean isUrl(String name) {
        if (!name.contains("://")) {
            return false;
        }
        try {
            new URL(name);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
}
