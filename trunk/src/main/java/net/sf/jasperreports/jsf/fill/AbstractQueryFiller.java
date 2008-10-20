/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.fill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;

import net.sf.jasperreports.jsf.JRFacesException;

// TODO: Auto-generated Javadoc
/**
 * The Class QueryFiller.
 */
public abstract class AbstractQueryFiller extends AbstractFiller {

    /**
     * Execute query.
     * 
     * @param conn the conn
     * 
     * @return the result set
     * 
     * @throws JRFacesException the JR faces exception
     * @throws FillerException the filler exception
     */
    protected ResultSet executeQuery(final Connection conn)
            throws FillerException {
        PreparedStatement st = null;
        final String query = getDataSourceComponent().getQuery();
        try {
            int paramIdx = 1;
            st = conn.prepareStatement(query);
            for (final UIComponent kid : getDataSourceComponent().getChildren()) {
                if (!(kid instanceof UIParameter)) {
                    continue;
                }
                final UIParameter param = (UIParameter) kid;
                st.setObject(paramIdx++, param.getValue());
            }
            return st.executeQuery();
        } catch (final SQLException e) {
            throw new FillerException(e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (final SQLException e) {
                    // ignore
                }
            }
        }
    }

}
