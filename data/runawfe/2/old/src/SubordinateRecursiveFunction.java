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

import ru.runa.af.organizationfunction.dao.OrganizationHierarchyDAO;
import ru.runa.af.organizationfunction.dao.Resources;

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
}
