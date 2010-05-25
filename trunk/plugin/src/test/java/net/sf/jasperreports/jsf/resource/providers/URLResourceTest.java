/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource.providers;

import java.net.URL;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author aalonsodominguez
 */
public class URLResourceTest {

    public static final String RES_LOCATION =
            "http://jasperreportjsf.sourceforge.net/tld/jasperreports-jsf-1_0.tld";

    private URL location;
    private URLResource resource;

    @BeforeTest
    public void createResource() throws Exception {
        location = new URL(RES_LOCATION);
        resource = new URLResource(RES_LOCATION, location);
    }

    @Test
    public void getName() {
        String resName = resource.getName();
        assert resName != null;
        assert RES_LOCATION.equals(resName);
    }

    @Test
    public void getLocation() throws Exception {
        URL loc = resource.getLocation();
        assert loc != null;
        assert location.equals(loc);
    }

    @Test
    public void getPath() {
        String path = resource.getPath();
        assert path != null;
        assert location.getPath().equals(path);
    }

}
