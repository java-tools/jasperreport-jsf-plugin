/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource.providers;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aalonsodominguez
 */
public class ContextResourceTest {

    public static final String RES_NAME = "";

    private ContextResource resource;

    @Before
    public void createResource() {
        resource = new ContextResource(RES_NAME);
    }

    @Test
    public void getName() {
        String name = resource.getName();
        Assert.assertNotNull(name);
        Assert.assertEquals(RES_NAME, name);
    }

}
