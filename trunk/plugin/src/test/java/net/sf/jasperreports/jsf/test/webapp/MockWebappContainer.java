/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test.webapp;

/**
 *
 * @author antonio.alonso
 */
public abstract class MockWebappContainer {

    public abstract boolean isStarted();

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

}
