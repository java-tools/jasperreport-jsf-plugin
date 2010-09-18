/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.webapp;

import java.io.File;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 *
 * @author aalonsodominguez
 */
public class MockWebappServletContainer extends MockWebappContainer {

    public static final String DEFAULT_WEBXML = "WEB-INF/web.xml";

    private String contextDir;
    private String contextPath;
    private File webXml;

    private ServletRunner runner = null;
    private boolean started = false;

    public MockWebappServletContainer(String contextDir, String contextPath) {
        this(contextDir, contextPath, DEFAULT_WEBXML);
    }

    public MockWebappServletContainer(String contextDir, String contextPath,
            String webXml) {
        this.contextDir = contextDir;
        this.contextPath = contextPath;
        this.webXml = new File(contextDir, webXml);
    }

    public String getContextPath() {
        return contextPath;
    }

    public File getWebXml() {
        return webXml;
    }

    public boolean isStarted() {
        return started;
    }

    public ServletUnitClient createClient() {
        if (!started) {
            throw new IllegalArgumentException("ServletRunner not started");
        }

        return runner.newClient();
    }

    public void start() throws Exception {
        if (started) {
            throw new IllegalArgumentException("ServletRunner already started");
        }

        runner = new ServletRunner(webXml, contextPath);
        started = true;
    }

    public void stop() {
        if (!started) {
            throw new IllegalArgumentException("ServletRunner not started");
        }

        runner.shutDown();
        runner = null;
        started = false;
    }

}
