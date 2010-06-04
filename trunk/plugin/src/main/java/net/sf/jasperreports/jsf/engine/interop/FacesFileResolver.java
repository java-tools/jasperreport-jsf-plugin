/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.engine.interop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;

public class FacesFileResolver implements FileResolver {

    private static final int BUFFER_SIZE = 2048;
    
    private final UIReport report;

    public FacesFileResolver(final UIReport report) {
        super();
        if (report == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        this.report = report;
    }

    public File resolveFile(final String name) {
        File resultFile;

        Resource resource;
        try {
            resource = resolveResource(name);
            if (isRemote(resource)) {
                resultFile = downloadResource(resource);
            } else {
                resultFile = new File(resource.getName());
            }
        } catch (final IOException e) {
            throw new JRFacesException(e);
        }

        return resultFile;
    }

    protected Resource resolveResource(String name) throws IOException {
        return getJRFacesContext().createResource(getFacesContext(),
                report, name);
    }

    protected File downloadResource(Resource resource) throws IOException {
        File tempFile = File.createTempFile(
                resource.getLocation().getFile(), null);
        InputStream is = resource.getInputStream();
        OutputStream os = new FileOutputStream(tempFile);

        int read;
        byte[] buff = new byte[BUFFER_SIZE];
        while (0 > (read = is.read(buff))) {
            os.write(buff, 0, read);
        }

        return tempFile;
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

    protected boolean isRemote(Resource resource) throws IOException {
        URL resourceURL = resource.getLocation();
        if (!"file".equals(resourceURL.getProtocol())) {
            ExternalContextHelper helper = getJRFacesContext()
                    .getExternalContextHelper(getFacesContext());
            return !(resourceURL.getHost().equals(helper.getRequestServerName(
                    getFacesContext().getExternalContext())));
        }
        return false;
    }
    
}