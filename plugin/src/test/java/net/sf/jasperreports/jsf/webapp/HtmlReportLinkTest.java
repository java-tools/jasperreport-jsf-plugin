/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.webapp;

import net.sf.jasperreports.jsf.Constants;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class HtmlReportLinkTest {

	private static final Logger logger = Logger.getLogger(
			HtmlReportLinkTest.class.getName(), 
			Constants.LOG_MESSAGES_BUNDLE);

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .setWebXML(new File("src/test/webapp/WEB-INF/web.xml"))
                .addAsResource(new File("src/test/webapp", "HtmlReportLinkTest.jsp"))
                .addAsWebInfResource(new File("src/test/webapp/WEB-INF", "faces-config.xml"));
    }

    @Test
    public void clickOnLink() throws Exception {

    }
}
