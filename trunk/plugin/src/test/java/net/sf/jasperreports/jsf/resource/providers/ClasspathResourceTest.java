/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource.providers;

import java.net.URL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aalonsodominguez
 */
public class ClasspathResourceTest {

    private static final String RES_NAME =
            ClasspathResourceTest.class.getPackage().getName().replaceAll("\\.", "/") +
            "/" + ClasspathResourceTest.class.getSimpleName() + ".txt";

    private ClassLoader classLoader;
    private ClasspathResource resource;

    @Before
    public void createResource() {
        classLoader = Thread.currentThread().getContextClassLoader();
        resource = new ClasspathResource(RES_NAME, classLoader);
    }

    @Test
    public void getName() {
        String name = resource.getName();
        Assert.assertNotNull(name);
        Assert.assertEquals(RES_NAME, name);
    }

    @Test
    public void getLocation() throws Exception {
        URL expected = classLoader.getResource(RES_NAME);
        URL loc = resource.getLocation();
        Assert.assertNotNull(loc);
        Assert.assertEquals(expected, loc);
    }

}
