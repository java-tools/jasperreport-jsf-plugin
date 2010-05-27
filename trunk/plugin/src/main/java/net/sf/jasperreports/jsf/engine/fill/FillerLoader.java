/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.fill;

import net.sf.jasperreports.jsf.util.Services;

/**
 *
 * @author aalonsodominguez
 */
public final class FillerLoader {

    private static final Filler DEFAULT_FILLER = new DefaultFiller();

    public static Filler getFiller() {
        return Services.chain(Filler.class, DEFAULT_FILLER);
    }

    private FillerLoader() { }

}
