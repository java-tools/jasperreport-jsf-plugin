/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.framework;

import net.sf.jasperreports.jsf.test.framework.ProfilerTestCase;
import com.meterware.httpunit.WebResponse;
import java.util.logging.Logger;

/**
 *
 * @author aalonsodominguez
 */
public abstract class ComponentProfilerTestCase extends ProfilerTestCase {

    private static final Logger logger = Logger.getLogger(
            ComponentProfilerTestCase.class.getPackage().getName());

    public WebResponse getComponentView() throws Exception {
        String testClassName = this.getClass().getSimpleName();
        StringBuilder facesURI = new StringBuilder();
        facesURI.append("/").append(testClassName).append(".jsf");
        logger.fine("Requesting component view: " + facesURI);
        return getResponse(facesURI.toString());
    }

}
