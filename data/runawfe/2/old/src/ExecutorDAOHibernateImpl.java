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
 * */
package ru.runa.af.dao.impl;

import gnu.trove.TLongObjectHashMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Conjunction;
import net.sf.hibernate.expression.Criterion;
import net.sf.hibernate.expression.Example;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.MatchMode;
import ru.runa.af.Actor;
import ru.runa.af.Executor;
import ru.runa.af.ExecutorAlreadyExistsException;
import ru.runa.af.ExecutorAlreadyInGroupException;
import ru.runa.af.ExecutorNotInGroupException;
import ru.runa.af.ExecutorOutOfDateException;
import ru.runa.af.Group;
import ru.runa.af.InternalApplicationException;
import ru.runa.af.Permission;
import ru.runa.af.PermissionNotFoundException;
import ru.runa.af.SecuredObject;
import ru.runa.af.SecuredObjectAlreadyExistsException;
import ru.runa.af.SecuredObjectOutOfDateException;
import ru.runa.af.UnapplicablePermissionException;
import ru.runa.af.dao.DAOFactory;
import ru.runa.af.dao.ExecutorDAO;
import ru.runa.af.dao.SecuredObjectDAO;
import ru.runa.af.dao.impl.idgen.DescendingPropertyIdGenerator;
import ru.runa.af.presentation.BatchPresentation;
import ru.runa.commons.ArraysCommons;
import ru.runa.commons.SQLCommons;
import ru.runa.commons.hibernate.HibernateSessionFactory;

/**
 * Created on 16.12.2004
 * 
 * @author Vitaliy S aka Yilativs<a href="mailto:runawfe@gmail.com">
 * @author Gordienko_m@runa.ru
 */
public class ExecutorDAOHibernateImpl extends DAOHibernateImpl implements ExecutorDAO {

	public ExecutorDAOHibernateImpl() throws InternalApplicationException {
		super();
	}

	private static final String NAME_PROPERTY_NAME = "name";

	private static final String ID_PROPERTY_NAME = "id";

	private static final String ACTOR_ID_PROPERTY_NAME = "actorId";

	private static final String EXECUTOR_PROPERTY_NAME = "executor";

	private static final String GROUP_PROPERTY_NAME = "group";

	private static final String SECURED_OBJECT_PROPERTY_NAME = "securedObject";

	private static final String CODE_PROPERTY_NAME = "code";

	private static final String MASK_PROPERTY_NAME = "mask";

	public boolean isExecutorExists(Executor executor) throws InternalApplicationException {
		checkIdentifiableNotNull(executor);
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(Executor.class);
			criteria.add(Expression.eq(NAME_PROPERTY_NAME, executor.getName()));
			List result = criteria.list();
			if (result.size() > 0) {
				return true;
			}
			return false;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public Executor getExecutor(Executor executor) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(executor);
		return checkExecutorUpToDate(executor);
	}

	public Executor[] create(Executor[] executors) throws InternalApplicationException, ExecutorAlreadyExistsException {
		Executor[] newExecutors = new Executor[executors.length];
		for (int i = 0; i < executors.length; i++) {
			newExecutors[i] = create(executors[i]);
		}
		return newExecutors;
	}

	public Executor create(Executor executor) throws InternalApplicationException, ExecutorAlreadyExistsException {
		checkIdentifiableNotNull(executor);
		SecuredObjectDAO soDAO = null;
		boolean commit = false;
		Session session = HibernateSessionFactory.getSession();
		try {
			if (isExecutorExist(session, executor.getName())) {
				throw new ExecutorAlreadyExistsException(executor.getName());
			}
			if (executor instanceof Actor) {
				Actor actor = (Actor) executor;
				if (actor.getCode() == 0) {
					actor.setCode(DescendingPropertyIdGenerator.generate(session, actor, CODE_PROPERTY_NAME));
				}
				if (isExecutorExist(session, actor.getCode())) {
					throw new ExecutorAlreadyExistsException(actor.getCode());
				}
			}
			session.save(executor);
			soDAO = getSecuredObjectDAO();
			soDAO.create(executor);
			commit = true;
		} catch (SecuredObjectAlreadyExistsException e) {
			throw new InternalApplicationException(e);
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		} finally {
			DAOHelper.close(commit, soDAO);
		}
		return executor;
	}

	public void remove(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException {
		checkExecutorsIdNotNull(ids);
		Executor[] executors = getExecutors(ids);
		remove(executors);
	}

	public void remove(Executor[] executors) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiablesNotNull(executors);

		for (int i = 0; i < executors.length; i++) {
			remove(executors[i]);
		}

	}

	private List<ExecutorGroupRelation> getExecutorGroupRelationsForExecutorList(Executor executor) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(ExecutorGroupRelation.class);
			criteria.add(Expression.eq(EXECUTOR_PROPERTY_NAME, executor));
			return criteria.list();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private List<ExecutorGroupRelation> getExecutorGroupRelationsForExecutorsList(List<Executor> executorList) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(ExecutorGroupRelation.class);
			criteria.add(Expression.in(EXECUTOR_PROPERTY_NAME, executorList));
			return criteria.list();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private List<Executor> getExecutorGroupRelationsForGroupList(Group group) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(ExecutorGroupRelation.class);
			criteria.add(Expression.eq(GROUP_PROPERTY_NAME, group));
			return criteria.list();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private List<ExecutorGroupRelation> getExecutorGroupRelationsForGroupsList(List groupList) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(ExecutorGroupRelation.class);
			criteria.add(Expression.in(GROUP_PROPERTY_NAME, groupList));
			return criteria.list();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private ExecutorGroupRelation getExecutorGroupRelation(Group group, Executor executor) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Criteria criteria = session.createCriteria(ExecutorGroupRelation.class);
			criteria.add(Expression.eq(GROUP_PROPERTY_NAME, group));
			criteria.add(Expression.eq(EXECUTOR_PROPERTY_NAME, executor));
			return (ExecutorGroupRelation) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private void remove(Executor executor) throws InternalApplicationException {
		checkIdentifiableNotNull(executor);
		SecuredObjectDAO soDAO = null;
		boolean commit = false;
		Session session = HibernateSessionFactory.getSession();
		try {
			List groupMappingList = getExecutorGroupRelationsForExecutorList(executor);
			for (Iterator iter = groupMappingList.iterator(); iter.hasNext();) {
				ExecutorGroupRelation mapping = (ExecutorGroupRelation) iter.next();
				session.delete(mapping);
			}

			if (executor instanceof Group) {
				List groupMappingList2 = getExecutorGroupRelationsForGroupList((Group) executor);
				for (Iterator iter = groupMappingList2.iterator(); iter.hasNext();) {
					ExecutorGroupRelation mapping = (ExecutorGroupRelation) iter.next();
					session.delete(mapping);
				}
			} else {
				Criteria criteria = session.createCriteria(ActorPassword.class);
				criteria.add(Expression.eq(ACTOR_ID_PROPERTY_NAME, new Long(executor.getId())));
				ActorPassword actorPassword = (ActorPassword) criteria.uniqueResult();
				if (actorPassword != null) {
					session.delete(actorPassword);
				}
			}
			Criteria pmCriteria = session.createCriteria(PermissionMapping.class);
			pmCriteria.add(Expression.eq(EXECUTOR_PROPERTY_NAME, executor));
			List permissionMappingSet = pmCriteria.list();
			for (Iterator iter = permissionMappingSet.iterator(); iter.hasNext();) {
				PermissionMapping mapping = (PermissionMapping) iter.next();
				session.delete(mapping);
			}
			if (permissionMappingSet.size() > 0) {
				permissionMappingSet.clear();
			}
			soDAO = getSecuredObjectDAO();
			soDAO.remove(executor);
			session.delete(executor);
			commit = true;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		} catch (SecuredObjectOutOfDateException e) {
			throw new InternalApplicationException(e);
		} finally {
			DAOHelper.close(commit, soDAO);
		}
	}

	public Executor update(Executor oldExecutor, Executor newExecutor) throws InternalApplicationException, ExecutorAlreadyExistsException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(oldExecutor);
		checkIdentifiableNotNull(oldExecutor);
		if (!oldExecutor.getClass().equals(newExecutor.getClass())) {
			throw new InternalApplicationException("Old executor class " + oldExecutor.getClass() + " not equal to new executor class " + newExecutor.getClass());
		}
		Session session = HibernateSessionFactory.getSession();
		try {
			if (isExecutorExistExcludeId(session, newExecutor.getName(), oldExecutor.getId())) {
				throw new ExecutorAlreadyExistsException(newExecutor.getName());
			}
			oldExecutor.setName(newExecutor.getName());
			oldExecutor.setDescription(newExecutor.getDescription());
			if (oldExecutor instanceof Actor) {
				Actor oldActor = (Actor) oldExecutor;
				Actor newActor = (Actor) newExecutor;
				if (isExecutorExistExcludeId(session, newActor.getCode(), oldActor.getId())) {
					throw new ExecutorAlreadyExistsException(newActor.getCode());
				}
				oldActor.setFullName(newActor.getFullName());
				oldActor.setCode(newActor.getCode());
			}
			session.update(oldExecutor);
			return oldExecutor;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public Executor[] getAll(BatchPresentation batchPresentation) throws InternalApplicationException {
		List<Executor> executorsList = getAll(Executor.class, batchPresentation);
		return executorsList.toArray(new Executor[executorsList.size()]);
	}

	public Actor[] getAllActors(BatchPresentation batchPresentation) throws InternalApplicationException {
		List<Actor> actorList = getAll(Actor.class, batchPresentation);
		return actorList.toArray(new Actor[actorList.size()]);
	}

	public Group[] getAllGroups(BatchPresentation batchPresentation) throws InternalApplicationException {
		List<Group> groupList = getAll(Group.class, batchPresentation);
		return groupList.toArray(new Group[groupList.size()]);
	}

	private List getAll(Class clazz, BatchPresentation batchPresentation) throws InternalApplicationException {
		Session session = HibernateSessionFactory.getSession();
		try {
			Criteria criteria = session.createCriteria(clazz);
			applyPresentationToCriteria(criteria, batchPresentation);
			List<Executor> executorsList = criteria.list();
			return executorsList;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private void checkAndAddExecutorToGroup(Executor executor, Group group) throws ExecutorAlreadyInGroupException {
		try {
			if (getExecutorGroupRelation(group, executor) != null) {
				throw new ExecutorAlreadyInGroupException(executor.getName(), group.getName());
			}
			ExecutorGroupRelation mapping = new ExecutorGroupRelation(group, executor);
			Session session = HibernateSessionFactory.getSession();
			session.save(mapping);
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public void addExecutorsToGroup(Executor[] executors, Group group) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorAlreadyInGroupException {
		checkIdentifiablesNotNull(executors);
		checkIdentifiableNotNull(group);
		for (int i = 0; i < executors.length; i++) {
			checkAndAddExecutorToGroup(executors[i], group);
		}
	}

	public void addExecutorToGroups(Executor executor, Group[] groups) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorAlreadyInGroupException {
		checkIdentifiableNotNull(executor);
		checkIdentifiablesNotNull(groups);
		for (int i = 0; i < groups.length; i++) {
			checkAndAddExecutorToGroup(executor, groups[i]);
		}

	}

	public void removeExecutorsFromGroup(Executor[] executors, Group group) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorNotInGroupException {
		checkIdentifiablesNotNull(executors);
		checkIdentifiableNotNull(group);
		for (int i = 0; i < executors.length; i++) {
			checkAndRemoveExecutorFromGroup(executors[i], group);
		}
	}

	public void removeExecutorFromGroups(Executor executor, Group[] groups) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorNotInGroupException {
		checkIdentifiableNotNull(executor);
		checkIdentifiablesNotNull(groups);
		for (int i = 0; i < groups.length; i++) {
			checkAndRemoveExecutorFromGroup(executor, groups[i]);
		}
	}

	private void checkAndRemoveExecutorFromGroup(Executor executor, Group group) throws ExecutorNotInGroupException {
		try {
			ExecutorGroupRelation mapping = getExecutorGroupRelation(group, executor);
			if (mapping == null) {
				throw new ExecutorNotInGroupException(executor.getName(), group.getName());
			}
			Session session = HibernateSessionFactory.getSession();
			session.delete(mapping);
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public boolean isExecutorInGroup(Executor executor, Group group) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(executor);
		checkIdentifiableNotNull(group);
		Set set = new HashSet();
		getExecutorGroupsRecursive(executor, set);
		return set.contains(group);
	}

	public Executor[] getGroupChildren(Group group, BatchPresentation batchPresentation) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(group);
		List groupChildrenList = getExecutorGroupRelationsForGroupList(group);
		Set<Executor> childrenSet = new HashSet<Executor>(groupChildrenList.size());
		for (Iterator iter = groupChildrenList.iterator(); iter.hasNext();) {
			ExecutorGroupRelation mapping = (ExecutorGroupRelation) iter.next();
			childrenSet.add(mapping.getExecutor());
		}
		List<Executor> allExecutorList = new ArrayList<Executor>(Arrays.asList(getAll(batchPresentation)));
		allExecutorList.retainAll(childrenSet);
		return (Executor[]) allExecutorList.toArray(new Executor[allExecutorList.size()]);
	}

	public Executor[] getExecutorsCanBeAddedToGroup(Group group, BatchPresentation batchPresentation) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(group);
		List<Executor> executorList = new ArrayList<Executor>(Arrays.asList(getAll(batchPresentation)));
		List<Executor> groupMappingList = getExecutorGroupRelationsForGroupList(group);
		for (Iterator iter = groupMappingList.iterator(); iter.hasNext();) {
			ExecutorGroupRelation mapping = (ExecutorGroupRelation) iter.next();
			executorList.remove(mapping.getExecutor());
		}
		executorList.remove(group);
		return executorList.toArray(new Executor[executorList.size()]);
	}

	public Group[] getExecutorGroups(Executor executor, BatchPresentation batchPresentation) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(executor);
		Set executorGroupSet = getExecutorGroupSet(executor);
		List<Group> allGroupList = new ArrayList<Group>(Arrays.asList(getAllGroups(batchPresentation)));
		allGroupList.remove(executor);
		allGroupList.retainAll(executorGroupSet);
		return allGroupList.toArray(new Group[allGroupList.size()]);
	}

	private Set getExecutorGroupSet(Executor executor) {
		List<ExecutorGroupRelation> groupMappingList = getExecutorGroupRelationsForExecutorList(executor);
		Set<Group> executorGroupSet = new HashSet<Group>(groupMappingList.size());
		for (ExecutorGroupRelation mapping : groupMappingList) {
			executorGroupSet.add(mapping.getGroup());
		}
		return executorGroupSet;
	}

	public long[] getActorAndGroupsIds(Actor actor) {
		checkIdentifiableNotNull(actor);
		Set groupSet = new HashSet();
		getExecutorGroupsRecursive(actor, groupSet);
		long[] ids = new long[groupSet.size() + 1];
		ids[0] = actor.getId();
		int i = 1;
		for (Iterator iter = groupSet.iterator(); iter.hasNext();) {
			Group group = (Group) iter.next();
			ids[i++] = group.getId();
		}
		return ids;
	}

	public void setPermissions(Executor executor, Permission[] permissions, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException, UnapplicablePermissionException {
		checkIdentifiableNotNull(executor);
		checkPermissionsNotNull(permissions);
		checkSecuredObjectNotNull(securedObject);

		Session session = HibernateSessionFactory.getSession();
		try {
			checkArePermissionAllowed(session, securedObject, permissions);

			setPermission(session, executor, permissions, securedObject);
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}

	}

	public void setPermissions(Executor[] executors, Permission[] permissions, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException, UnapplicablePermissionException {
		checkExecutorsNotNull(executors);
		checkPermissionsNotNull(permissions);
		checkSecuredObjectNotNull(securedObject);
		Session session = HibernateSessionFactory.getSession();
		try {
			checkArePermissionAllowed(session, securedObject, permissions);

			for (int i = 0; i < executors.length; i++) {
				setPermission(session, executors[i], permissions, securedObject);
			}
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private void checkArePermissionAllowed(Session session, SecuredObject securedObject, Permission[] permissions) throws InternalApplicationException, UnapplicablePermissionException {
		Permission[] applicablePermission = getNoPermission(session, securedObject.getType()).getAllPermissions();
		Permission[] notAllowedPermission = Permission.subtractPermissions(permissions, applicablePermission);

		if (notAllowedPermission.length > 0) {
			throw new UnapplicablePermissionException(securedObject.getType(), securedObject.getExtId(), permissions);
		}
	}

	private void setPermission(Session session, Executor executor, Permission[] permissions, SecuredObject securedObject) throws InternalApplicationException, HibernateException {
		Set<PermissionMapping> permissionMappingToRemoveSet = new HashSet<PermissionMapping>(getOwnPermissionMappings(session, executor, securedObject));
		for (int i = 0; i < permissions.length; i++) {
			long mask = permissions[i].getMask();
			PermissionMapping pm = new PermissionMapping(executor, securedObject, mask);
			if (permissionMappingToRemoveSet.contains(pm)) {
				permissionMappingToRemoveSet.remove(pm);
			} else {
				session.save(pm);
			}
		}
		for (PermissionMapping permissionMapping : permissionMappingToRemoveSet) {
			session.delete(permissionMapping);
		}
	}

	public void setPermissions(Executor[] executors, Permission[][] permissions, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException, UnapplicablePermissionException {
		checkExecutorsNotNull(executors);
		checkPermissionsNotNull(permissions);
		checkHaveSameLength(executors, permissions);

		checkSecuredObjectNotNull(securedObject);
		Session session = HibernateSessionFactory.getSession();
		try {
			for (int i = 0; i < executors.length; i++) {
				checkArePermissionAllowed(session, securedObject, permissions[i]);
				setPermission(session, executors[i], permissions[i], securedObject);
			}
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private void checkHaveSameLength(Executor[] executors, Permission[][] permissions) {
		if (executors.length != permissions.length) {
			throw new IllegalArgumentException("arrays length differs");
		}
	}

	public boolean isAllowed(Executor executor, Permission permission, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException {
		checkIdentifiableNotNull(executor);
		checkPermissionNotNull(permission);
		checkSecuredObjectNotNull(securedObject);
		Session session = HibernateSessionFactory.getSession();

		boolean isAllowed = false;
		try {
			Criteria criteria = session.createCriteria(PermissionMapping.class);
			Conjunction conjunction = Expression.conjunction();
			conjunction.add(Expression.eq(SECURED_OBJECT_PROPERTY_NAME, securedObject));
			conjunction.add(Expression.eq(MASK_PROPERTY_NAME, new Long(permission.getMask())));
			Criterion criterion = createExecutorAndAllHisGroupCriterion(executor);
			conjunction.add(criterion);
			criteria.add(conjunction);
			isAllowed = criteria.list().size() > 0;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
		return isAllowed;
	}

	private Criterion createExecutorAndAllHisGroupCriterion(Executor executor) throws InternalApplicationException {
		Set<Executor> set = new HashSet<Executor>();
		getExecutorGroupsRecursive(executor, set);
		set.add(executor);
		return Expression.in(EXECUTOR_PROPERTY_NAME, set);
	}

	public boolean[] isAllowed(Executor executor, Permission permission, SecuredObject[] securedObjects) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException {
		checkIdentifiableNotNull(executor);
		checkPermissionNotNull(permission);
		checkSecuredObjectsNotNull(securedObjects);
		boolean[] result = new boolean[securedObjects.length];
		if (securedObjects.length == 0) {
			return result;
		}
		Session session = HibernateSessionFactory.getSession();

		try {
			Criteria criteria = session.createCriteria(PermissionMapping.class);
			Conjunction conjunction = Expression.conjunction();
			conjunction.add(Expression.in(SECURED_OBJECT_PROPERTY_NAME, securedObjects));
			conjunction.add(Expression.eq(MASK_PROPERTY_NAME, new Long(permission.getMask())));
			Criterion criterion = createExecutorAndAllHisGroupCriterion(executor);
			conjunction.add(criterion);
			criteria.add(conjunction);
			List list = criteria.list();

			Set<SecuredObject> allowedSecuredObjectSet = new HashSet<SecuredObject>(list.size());
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				PermissionMapping pm = (PermissionMapping) iter.next();
				allowedSecuredObjectSet.add(pm.getSecuredObject());
			}

			for (int i = 0; i < securedObjects.length; i++) {
				if (allowedSecuredObjectSet.contains(securedObjects[i])) {
					result[i] = true;
				}
			}
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}

		return result;
	}

	public Permission[] getPermissions(Executor executor, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException {
		checkIdentifiableNotNull(executor);
		checkSecuredObjectNotNull(securedObject);
		Session session = HibernateSessionFactory.getSession();
		List permissionMappingList = getPermissionMappings(session, executor, securedObject);
		try {
			return getPermission(permissionMappingList, getNoPermission(session, securedObject.getType()));
		} catch (PermissionNotFoundException e) {
			throw new InternalApplicationException(e);
		}

	}

	public Permission[] getOwnPermissions(Executor executor, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException {
		checkIdentifiableNotNull(executor);
		checkSecuredObjectNotNull(securedObject);
		Session session = HibernateSessionFactory.getSession();
		List permissionMappingList = getOwnPermissionMappings(session, executor, securedObject);

		try {
			return getPermission(permissionMappingList, getNoPermission(session, securedObject.getType()));
		} catch (PermissionNotFoundException e) {
			throw new InternalApplicationException(e);
		}
	}

	public Actor getActor(String name) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Actor) getExecutor(Actor.class, name);
	}

	public Actor getActor(long id) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Actor) getExecutor(Actor.class, id);
	}

	public Executor[] getExecutors(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException {
		return getExecutors(Executor.class, ids, ID_PROPERTY_NAME);
	}

	public Group[] getGroups(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Group[]) getExecutors(Group.class, ids, ID_PROPERTY_NAME);
	}

	public Actor[] getActorsByCodes(long[] codes) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Actor[]) getExecutors(Actor.class, codes, CODE_PROPERTY_NAME);
	}

	private static final String GET_ACTORS_IDS_BY_CODES = "select actor.id from Actor as actor where actor.code";
	
	/**
	 * 
	 * @param codes
	 * @return actor codes with probably different order
	 * @throws InternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	public long[] getActorsIdByCodes(long[] codes) throws InternalApplicationException, ExecutorOutOfDateException {
		if (codes.length == 0) {
			return new long[0];
		}
		Session session = HibernateSessionFactory.getSession();
		try {
			Query query = session.createQuery(GET_ACTORS_IDS_BY_CODES + SQLCommons.buildINQueryPart(codes));
			List<Long> codeList = query.list();
			if (codeList.size()!=codes.length){
				throw new ExecutorOutOfDateException("unknown actor",Actor.class);
			}
			long[] result = new long[codeList.size()];
			for (int i = 0; i < result.length; i++) {
				result[i]=codeList.get(i);
			}
			return result;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		} 
	}

	private static final String IS_ACTOR_ACTIVE = "select actor.active from Actor as actor where actor.code = :"+CODE_PROPERTY_NAME;
	
	public boolean isActorActive(long code) throws InternalApplicationException, ExecutorOutOfDateException {
		Session session = HibernateSessionFactory.getSession();
		try {
			Query query = session.createQuery(IS_ACTOR_ACTIVE);
			query.setParameter(CODE_PROPERTY_NAME, code);
			Boolean isActive = (Boolean)query.uniqueResult();
			if (isActive==null){
				throw new ExecutorOutOfDateException("unknown actor",Actor.class);
			}
			return isActive;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		} 
	}
	
	private static final String NOT_ACTIVE_ACTOR_CODES = "select actor.code from Actor as actor where actor.active = 0";
	public long[] getNotActiveActorCodes() throws InternalApplicationException {
		Session session = HibernateSessionFactory.getSession();
		try {
			Query query = session.createQuery(NOT_ACTIVE_ACTOR_CODES);
			return ArraysCommons.createLongArray(query.list());
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		} 
	}
	
	public Actor[] getActors(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Actor[]) getExecutors(Actor.class, ids, ID_PROPERTY_NAME);
	}

	/**
	 * @param clazz -
	 *            class of executors to select {@link Actor}{@link Executor}{@link Group}
	 * @param identifiers -
	 *            idetifers to use can be code[] or ids[]
	 * @param propertyName
	 * @return
	 * @throws InternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	private Executor[] getExecutors(Class clazz, long[] identifiers, String propertyName) throws InternalApplicationException, ExecutorOutOfDateException {
		if (identifiers.length == 0) {
			return (Executor[]) Array.newInstance(clazz, 0);
		}
		Session session = HibernateSessionFactory.getSession();
		try {
			Criteria criteria = session.createCriteria(clazz);
			Long[] longIdentifiers = new Long[identifiers.length];
			for (int i = 0; i < identifiers.length; i++) {
				longIdentifiers[i] = new Long(identifiers[i]);
			}
			criteria.add(Expression.in(propertyName, longIdentifiers));
			List<Executor> executorList = criteria.list();

			TLongObjectHashMap<Executor> identifierExecutorMap = new TLongObjectHashMap<Executor>(executorList.size());
			for (Executor executor: executorList) {
				if (propertyName==ID_PROPERTY_NAME){
					identifierExecutorMap.put(executor.getId(), executor);
				}else{
					identifierExecutorMap.put(((Actor)executor).getCode(), executor);
				}
			}

			Executor[] executors = (Executor[]) Array.newInstance(clazz, identifiers.length);
			for (int i = 0; i < executors.length; i++) {
				Executor executor = identifierExecutorMap.get(longIdentifiers[i]);
				if (executor == null) {
					throw new ExecutorOutOfDateException("with identifier " + identifiers[i] + " for property " + propertyName, clazz);
				}
				executors[i] = executor;
			}
			return executors;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		} 
	}

	public Executor getExecutor(String name) throws InternalApplicationException, ExecutorOutOfDateException {
		return getExecutor(Executor.class, name);
	}

	private Executor getExecutor(Class clazz, long id) throws InternalApplicationException, ExecutorOutOfDateException {
		Session session = HibernateSessionFactory.getSession();
		try {
			Criteria criteria = session.createCriteria(clazz);
			criteria.add(Expression.eq(ID_PROPERTY_NAME, new Long(id)));
			Executor executor = (Executor) criteria.uniqueResult();
			if (executor == null) {
				throw new ExecutorOutOfDateException(id, clazz);
			}
			return executor;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private Executor getExecutor(Class clazz, String name) throws InternalApplicationException, ExecutorOutOfDateException {
		checkNameNotNull(name);
		Session session = HibernateSessionFactory.getSession();
		try {
			Criteria criteria = session.createCriteria(clazz);
			criteria.add(Expression.eq(NAME_PROPERTY_NAME, name));

			Executor executor = (Executor) criteria.uniqueResult();
			if (executor == null) {
				throw new ExecutorOutOfDateException(name, clazz);
			}
			return executor;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public Executor getExecutor(long id) throws InternalApplicationException, ExecutorOutOfDateException {
		return getExecutor(Executor.class, id);
	}

	public Group getGroup(String name) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Group) getExecutor(Group.class, name);
	}

	public Group getGroup(long id) throws InternalApplicationException, ExecutorOutOfDateException {
		return (Group) getExecutor(Group.class, id);
	}

	public void setPassword(Actor actor, String password) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(actor);
		checkPasswordNotNull(password);
		Session session = HibernateSessionFactory.getSession();
		try {
			Criteria criteria = session.createCriteria(ActorPassword.class);
			criteria.add(Expression.eq(ACTOR_ID_PROPERTY_NAME, new Long(actor.getId())));
			ActorPassword actorPassword = (ActorPassword) criteria.uniqueResult();
			if (actorPassword == null) {
				actorPassword = new ActorPassword(actor, password);
				session.save(actorPassword);
			} else {
				actorPassword.setPassword(password);
			}
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public void setStatus(Actor actor, boolean isActive) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(actor);
		Session session = HibernateSessionFactory.getSession();
		try {
			actor.setActive(isActive);
			session.update(actor);
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}

	}

	public boolean isPasswordValid(Actor actor, String password) throws InternalApplicationException, ExecutorOutOfDateException {
		checkIdentifiableNotNull(actor);
		checkPasswordNotNull(password);
		Session session = HibernateSessionFactory.getSession();
		try {
			ActorPassword actorPassword = new ActorPassword(actor, password);
			Criteria criteria = session.createCriteria(ActorPassword.class);
			criteria.add(Example.create(actorPassword));
			criteria.add(Expression.eq(ACTOR_ID_PROPERTY_NAME, new Long(actor.getId())));
			Object result = criteria.uniqueResult();
			return (result == null) ? false : true;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private boolean isExecutorExist(Session session, String executorName) throws InternalApplicationException {
		try {
			Criteria criteria = session.createCriteria(Executor.class);
			criteria.add(Expression.eq(NAME_PROPERTY_NAME, executorName));
			return criteria.list().size() > 0;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private boolean isExecutorExist(Session session, long code) throws InternalApplicationException {
		try {
			Criteria criteria = session.createCriteria(Actor.class);
			criteria.add(Expression.eq(CODE_PROPERTY_NAME, new Long(code)));
			return criteria.list().size() > 0;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}

	}

	private boolean isExecutorExistExcludeId(Session session, String executorName, long id) throws InternalApplicationException {
		try {
			Criteria criteria = session.createCriteria(Executor.class);
			criteria.add(Expression.eq(NAME_PROPERTY_NAME, executorName));
			criteria.add(Expression.not(Expression.eq(ID_PROPERTY_NAME, new Long(id))));
			return criteria.list().size() > 0;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private boolean isExecutorExistExcludeId(Session session, long code, long id) throws InternalApplicationException {
		try {
			Criteria criteria = session.createCriteria(Actor.class);
			criteria.add(Expression.eq(CODE_PROPERTY_NAME, new Long(code)));
			criteria.add(Expression.not(Expression.eq(ID_PROPERTY_NAME, new Long(id))));
			return criteria.list().size() > 0;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private SecuredObjectDAO getSecuredObjectDAO() throws InternalApplicationException {
		return DAOFactory.getInstance().createSecuredObjectDAO();
	}

	private void getExecutorGroupsRecursive(Executor executor, Set<Executor> executorAndGroupSet) throws InternalApplicationException {
		List<Executor> executorList = new ArrayList<Executor>(1);
		executorList.add(executor);
		getExecutorsGroupsRecursive(executorList, executorAndGroupSet);
	}

	private void getExecutorsGroupsRecursive(List<Executor> executorList, Set<Executor> executorAndGroupSet) throws InternalApplicationException {
		List<ExecutorGroupRelation> groupMappingList = getExecutorGroupRelationsForExecutorsList(executorList);
		List<Executor> uncheckedGroups = new ArrayList<Executor>(groupMappingList.size());
		for (ExecutorGroupRelation mapping : groupMappingList) {
			Group group = mapping.getGroup();
			if (!executorAndGroupSet.contains(group)) {
				executorAndGroupSet.add(group);
				uncheckedGroups.add(group);
			}
		}
		if (!uncheckedGroups.isEmpty()) {
			getExecutorsGroupsRecursive(uncheckedGroups, executorAndGroupSet);
		}
	}

	private List getOwnPermissionMappings(Session session, Executor executor, SecuredObject securedObject) throws InternalApplicationException {
		try {
			Criteria criteria = session.createCriteria(PermissionMapping.class);
			criteria.add(Expression.eq(SECURED_OBJECT_PROPERTY_NAME, securedObject));
			criteria.add(Expression.eq(EXECUTOR_PROPERTY_NAME, executor));
			return criteria.list();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private List getPermissionMappings(Session session, Executor executor, SecuredObject securedObject) throws InternalApplicationException {
		try {
			Criteria criteria = session.createCriteria(PermissionMapping.class);
			Conjunction conjunction = Expression.conjunction();
			conjunction.add(Expression.eq(SECURED_OBJECT_PROPERTY_NAME, securedObject));
			Criterion criterion = createExecutorAndAllHisGroupCriterion(executor);
			conjunction.add(criterion);
			criteria.add(conjunction);
			return criteria.list();
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	private Permission[] getPermission(Collection<PermissionMapping> permissionMappings, Permission permission) throws PermissionNotFoundException {
		Set<Permission> permissionSet = new HashSet<Permission>();
		for (PermissionMapping pm : permissionMappings) {
			permissionSet.add(permission.getPermission(pm.getMask()));
		}
		return permissionSet.toArray(new Permission[permissionSet.size()]);
	}

	public Actor getActorByCode(long code) throws InternalApplicationException, ExecutorOutOfDateException {
		Session session = HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Actor.class);
		criteria.add(Expression.eq(CODE_PROPERTY_NAME, new Long(code)));
		try {
			Actor actor = (Actor) criteria.uniqueResult();
			if (actor == null) {
				throw new ExecutorOutOfDateException("with code " + code, Actor.class);
			}
			return actor;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public boolean isActorExist(long code) throws InternalApplicationException {
		Session session = HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Actor.class);
		criteria.add(Expression.eq(CODE_PROPERTY_NAME, new Long(code)));
		try {
			Actor actor = (Actor) criteria.uniqueResult();
			return (actor == null) ? false : true;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public Actor getActorCaseInsensitive(String name) throws InternalApplicationException, ExecutorOutOfDateException {
		checkNameNotNull(name);
		Session session = HibernateSessionFactory.getSession();
		try {
			Criteria criteria = session.createCriteria(Actor.class);
			criteria.add(Expression.ilike(NAME_PROPERTY_NAME, name, MatchMode.EXACT));
			Actor actor = (Actor) criteria.uniqueResult();
			if (actor == null) {
				throw new ExecutorOutOfDateException(name, Actor.class);
			}
			return actor;
		} catch (HibernateException e) {
			throw new InternalApplicationException(e);
		}
	}

	public Actor[] getActorsByExecutorIds(long[] executorIds) throws InternalApplicationException, ExecutorOutOfDateException {
		checkExecutorsIdNotNull(executorIds);
		Executor[] executors = getExecutors(executorIds);
		Set<Actor> actorSet = new HashSet<Actor>();
		Set<Group> visitedGroupSet = new HashSet<Group>();
		getActorsInGroupsRecursive(Arrays.asList(executors), actorSet, visitedGroupSet);
		return actorSet.toArray(new Actor[actorSet.size()]);
	}

	private void getActorsInGroupsRecursive(List<Executor> executors, Set<Actor> actorSet, Set<Group> visitedGroupSet) {
		List<Executor> unvisitedGroups = new ArrayList<Executor>();
		for (Iterator iter = executors.iterator(); iter.hasNext();) {
			Executor executor = (Executor) iter.next();
			if (executor instanceof Group) {
				if (!visitedGroupSet.contains(executor)) {
					visitedGroupSet.add((Group) executor);
					unvisitedGroups.add(executor);
				}
			} else {
				actorSet.add((Actor) executor);
			}
		}
		if (!unvisitedGroups.isEmpty()) {
			List<ExecutorGroupRelation> groupMappingList = getExecutorGroupRelationsForGroupsList(unvisitedGroups);
			unvisitedGroups.clear();
			for (ExecutorGroupRelation mapping : groupMappingList) {
				unvisitedGroups.add(mapping.getExecutor());
			}
			getActorsInGroupsRecursive(unvisitedGroups, actorSet, visitedGroupSet);
		}
	}
}
