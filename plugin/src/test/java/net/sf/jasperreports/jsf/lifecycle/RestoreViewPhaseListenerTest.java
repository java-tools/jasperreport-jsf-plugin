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
package net.sf.jasperreports.jsf.lifecycle;

import static org.easymock.EasyMock.*;

import net.sf.jasperreports.jsf.test.LifecycleTestHarness;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author antonio.alonso
 */
public class RestoreViewPhaseListenerTest extends LifecycleTestHarness {

    private RestoreViewPhaseListener listener;

    @Before
    public void initLifecyce() {

        lifecycle.addPhaseListener(listener);
    }

    @After
    public void disposeLifecycle() {
        lifecycle.removePhaseListener(listener);
    }

    @Test
    public void testRenderComponent() {
        
    }

}
