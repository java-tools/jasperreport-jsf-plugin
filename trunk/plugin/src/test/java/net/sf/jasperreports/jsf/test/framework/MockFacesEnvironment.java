/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.framework;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.LifecycleFactory;

import org.apache.shale.test.mock.MockApplication12;
import org.apache.shale.test.mock.MockApplicationFactory;
import org.apache.shale.test.mock.MockExternalContext12;
import org.apache.shale.test.mock.MockFacesContext12;
import org.apache.shale.test.mock.MockFacesContextFactory;
import org.apache.shale.test.mock.MockLifecycle;
import org.apache.shale.test.mock.MockLifecycleFactory;
import org.apache.shale.test.mock.MockRenderKit;
import org.apache.shale.test.mock.MockRenderKitFactory;

/**
 *
 * @author aalonsodominguez
 */
public abstract class MockFacesEnvironment {

    private static ThreadLocal<MockFacesEnvironment> currentInstance =
            new ThreadLocal<MockFacesEnvironment>();
    
    public static MockFacesEnvironment getServletInstance() {
        MockFacesEnvironment instance = currentInstance.get();
        if (instance == null) {
            instance = new MockFacesServletEnvironment();
            currentInstance.set(instance);
        }
        return instance;
    }

    private MockApplication12 application;
    
    private MockFacesContext12 facesContext;
    private MockLifecycle lifecycle;
    private MockRenderKit renderKit;

    private MockApplicationFactory applicationFactory;
    private MockFacesContextFactory facesContextFactory;
    private MockLifecycleFactory lifecycleFactory;
    private MockRenderKitFactory renderKitFactory;

    protected MockFacesEnvironment() {
        FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                "org.apache.shale.test.mock.MockApplicationFactory");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                "org.apache.shale.test.mock.MockFacesContextFactory");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                "org.apache.shale.test.mock.MockLifecycleFactory");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                "org.apache.shale.test.mock.MockRenderKitFactory");

        initializeExternalContext();

        lifecycleFactory = (MockLifecycleFactory) FactoryFinder.getFactory(
                FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle) lifecycleFactory.getLifecycle(
                LifecycleFactory.DEFAULT_LIFECYCLE);

        facesContextFactory = (MockFacesContextFactory) FactoryFinder
                .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext12) facesContextFactory.getFacesContext(
                getExternalContext().getContext(),
                getExternalContext().getRequest(),
                getExternalContext().getResponse(), lifecycle);

        UIViewRoot viewRoot = new UIViewRoot();
        viewRoot.setViewId("/viewId");
        viewRoot.setRenderKitId(renderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(viewRoot);

        applicationFactory = (MockApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication12) applicationFactory.getApplication();
        facesContext.setApplication(application);

        renderKitFactory = (MockRenderKitFactory) FactoryFinder.getFactory(
                FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit(renderKitFactory.HTML_BASIC_RENDER_KIT,
                renderKit);
    }

    public MockApplication12 getApplication() {
        return application;
    }

    public MockApplicationFactory getApplicationFactory() {
        return applicationFactory;
    }

    public abstract MockExternalContext12 getExternalContext();

    public MockFacesContext12 getFacesContext() {
        return facesContext;
    }

    public MockFacesContextFactory getFacesContextFactory() {
        return facesContextFactory;
    }

    public MockLifecycle getLifecycle() {
        return lifecycle;
    }

    public MockLifecycleFactory getLifecycleFactory() {
        return lifecycleFactory;
    }

    public MockRenderKit getRenderKit() {
        return renderKit;
    }

    public MockRenderKitFactory getRenderKitFactory() {
        return renderKitFactory;
    }

    public void release() {
        releaseExternalContext();

        facesContext.release();
        application = null;
        lifecycle = null;
        renderKit = null;
        facesContext = null;

        applicationFactory = null;
        facesContextFactory = null;
        lifecycleFactory = null;
        renderKitFactory = null;
    }

    protected abstract void initializeExternalContext();

    protected abstract void releaseExternalContext();

}
