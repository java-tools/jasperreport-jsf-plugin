/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.util;

import java.io.Serializable;

/**
 *
 * @author aalonsodominguez
 */
public class PathString implements Serializable {

    public static final String PATH_SEPARATOR = "/";

    private PathString parent;
    private String element;

    public PathString(PathString parent, String element) {

    }

    public PathString(String[] pathElems) {

    }

    public String getElement() {
        return element;
    }

    public PathString getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (parent != null) {
            builder.append(parent.toString());
        }
        builder.append(PATH_SEPARATOR);
        builder.append(element);
        return builder.toString();

    }

}
