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
package net.sf.jasperreports.jsf.test.simple;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import net.sf.jasperreports.jsf.test.ReportTestBase;

@RunWith(Parameterized.class)
public class ReportLinkTest extends ReportTestBase {

    @Parameters
    public static Collection<?> linkIdentifiers() {
        return Arrays.asList(new Object[][]{{"reportForm:reportLink"}});
    }
    private final String linkId;

    public ReportLinkTest(final String linkId) {
        this.linkId = linkId;
    }

    @Test
    public void clickOnLink() throws Exception {
        final WebResponse response = getResponse("/ReportLinkTest.jsf");
        final WebLink link = response.getLinkWithID(linkId);
        Assert.assertNotNull("Link '" + linkId + "' is null", link);
        System.out.println("---> Here I click the link");
        link.click();
    }
}
