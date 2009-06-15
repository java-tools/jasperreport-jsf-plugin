/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.spi;

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.util.Util;
import net.sf.jasperreports.jsf.validation.Validator;

public final class ValidatorLoader {

	private static final Set<ValidatorFactory> validatorFactoryCache = Util
			.loadServiceSet(ValidatorFactory.class);

	public static Validator getValidator(final FacesContext context,
			final UIComponent component) {
		ValidatorFactory validatorFactory = null;
		for (final ValidatorFactory factory : validatorFactoryCache) {
			if (factory.acceptsComponent(component)) {
				validatorFactory = factory;
				break;
			}
		}

		if (validatorFactory == null) {
			throw new ValidatorFactoryNotFoundException(
					"No factory found for component: " + component.getFamily());
		}
		return validatorFactory.createValidator(context, component);
	}

	private ValidatorLoader() {
	}

}
