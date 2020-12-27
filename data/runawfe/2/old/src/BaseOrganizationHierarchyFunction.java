/*
 * This file is part of the RUNA WFE project.
 * Copyright (C) 2004-2006, Joint stock company "RUNA Technology"
 * All rights reserved.
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package ru.runa.af.organizationfunction;

import java.util.HashMap;
import java.util.Map;

import ru.runa.af.ExecutorOutOfDateException;
import ru.runa.af.InternalApplicationException;
import ru.runa.af.dao.DAOFactory;
import ru.runa.af.dao.ExecutorDAO;
import ru.runa.af.dao.impl.DAOHelper;
import ru.runa.commons.ArgumentsCommons;
import ru.runa.commons.StringLongValueCache;
import ru.runa.commons.TTLMapCleaner;

/**
 * 
 * Created on 08.01.2007
 * 
 * @author Vitaliy S <a href="mailto:runawfe@gmail.com">
 */
public abstract class BaseOrganizationHierarchyFunction implements OrganizationFunction {

	/**
	 * @param parameters
	 *            array of executor ids. Array size must be 1.
	 */
	public long[] getExecutorIds(Object[] parameters) throws OrganizationFunctionException {
		ArgumentsCommons.checkArrayLengthEQ(parameters, 1);
		try {
			Long code = CODE_AS_STRING_CODE_AS_LONG_CACHE.getLongValue((String) parameters[0]);
			long[] codes = getCodes(code);
			return getActorIdsByCodes(codes);
		} catch (NumberFormatException e) {
			throw new OrganizationFunctionException(e);
		} catch (InternalApplicationException e) {
			throw new OrganizationFunctionException(e);
		}
	}

	private static final Map<Long, Long> ACTOR_CODE_ACTOR_ACTOR_ID_CACHE_MAP = new HashMap<Long, Long>();

	private static final TTLMapCleaner ACTOR_CODE_ACTOR_ACTOR_ID_CLEANER = new TTLMapCleaner(ACTOR_CODE_ACTOR_ACTOR_ID_CACHE_MAP);

	private long[] getActorIdsByCodes(long[] codes) throws OrganizationFunctionException {
		ExecutorDAO executorDAO = DAOFactory.getInstance().createExecutorDAO();
		try {
			long[] ids = new long[codes.length];
			for (int i = 0; i < codes.length; i++) {
				Long id = ACTOR_CODE_ACTOR_ACTOR_ID_CACHE_MAP.get(codes[i]);
				if (id == null) {
					id = executorDAO.getActorsIdByCodes(new long[] { codes[i] })[0];
					ACTOR_CODE_ACTOR_ACTOR_ID_CACHE_MAP.put(codes[i], id);
				}
				ids[i] = id;
			}
			return ids;
		} catch (ExecutorOutOfDateException e) {
			throw new OrganizationFunctionException(e);
		} finally {
			DAOHelper.closeComitted(executorDAO);
		}
	}

	private static final StringLongValueCache CODE_AS_STRING_CODE_AS_LONG_CACHE = new StringLongValueCache();

	abstract long[] getCodes(Long code);
}
