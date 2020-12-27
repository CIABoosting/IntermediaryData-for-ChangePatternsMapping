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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.runa.af.ExecutorOutOfDateException;
import ru.runa.af.InternalApplicationException;
import ru.runa.af.dao.DAOFactory;
import ru.runa.af.dao.ExecutorDAO;
import ru.runa.af.dao.impl.DAOHelper;
import ru.runa.af.organizationfunction.dao.OrganizationHierarchyDAO;
import ru.runa.af.organizationfunction.dao.Resources;
import ru.runa.commons.ArgumentsCommons;

/**
 * 
 * Created on Jul 13, 2006
 * 
 * @author Vitaliy S <a href="mailto:runawfe@gmail.com">
 */
public class SubordinateRecursiveFunction extends BaseOrganizationHierarchyFunction {

	@Override
	protected long[] getCodes(Long code) {
		return OrganizationHierarchyDAO.getActorCodesRecurisve(Resources.getSubordinateCodesByChiefCodeSQL(), new Long[] { code });
	}
	
	protected long[] getActorIdsByCodes(long[] codes) throws OrganizationFunctionException {
		ExecutorDAO executorDAO = DAOFactory.getInstance().createExecutorDAO();
		try {
			List<Long> longIds = new ArrayList<Long>();
			for (int i = 0; i < codes.length; i++) {
				Long id = ACTOR_CODE_ACTOR_ACTOR_ID_CACHE_MAP.get(codes[i]);
				if (id == null) {
					long[] ids = executorDAO.getAvailableActorsIdByCodes(new long[] { codes[i] });
					if (ids.length > 0) {
						id = ids[0];
						ACTOR_CODE_ACTOR_ACTOR_ID_CACHE_MAP.put(codes[i], id);
						longIds.add(id);
					}
				}
			}
			long[] result = new long[longIds.size()];
			for (int i = 0; i < result.length; i++) {
				result[i]=longIds.get(i);
			}
			return result;
		} finally {
			DAOHelper.closeComitted(executorDAO);
		}
	}

}