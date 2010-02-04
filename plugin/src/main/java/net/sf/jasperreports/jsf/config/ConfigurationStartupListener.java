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
package net.sf.jasperreports.jsf.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfigurationStartupListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(
            ConfigurationStartupListener.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public void contextInitialized(ServletContextEvent sce) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "JRJSF_0022");
        }

        ServletContext context = sce.getServletContext();
        Configuration config = new Configuration(context);
        context.setAttribute(Configuration.INSTANCE_KEY, config);

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "JRJSF_0023");
        }
    }
}
