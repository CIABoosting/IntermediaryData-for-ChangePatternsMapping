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
package ru.runa.af.dao;

import ru.runa.af.Actor;
import ru.runa.af.Executor;
import ru.runa.af.ExecutorAlreadyExistsException;
import ru.runa.af.ExecutorAlreadyInGroupException;
import ru.runa.af.ExecutorNotInGroupException;
import ru.runa.af.ExecutorOutOfDateException;
import ru.runa.af.Group;
import ru.runa.af.InternalApplicationException;
import ru.runa.af.Permission;
import ru.runa.af.SecuredObject;
import ru.runa.af.SecuredObjectOutOfDateException;
import ru.runa.af.UnapplicablePermissionException;
import ru.runa.af.presentation.BatchPresentation;

/**
 * Created on 07.07.2004
 * 
 * @author Vitaliy S aka Yilativs<a href="mailto:runawfe@gmail.com">
 * @author Gordienko_m@runa.ru
 */
public interface ExecutorDAO extends ClosableDAO {
	public boolean isExecutorExists(Executor executor) throws InternalApplicationException;

	public Executor getExecutor(Executor executor) throws InternalApplicationException, ExecutorOutOfDateException;

	public Executor create(Executor executor) throws InternalApplicationException, ExecutorAlreadyExistsException;

	public Executor[] create(Executor[] executors) throws InternalApplicationException, ExecutorAlreadyExistsException;

	public void remove(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException;

	public void remove(Executor[] executors) throws InternalApplicationException, ExecutorOutOfDateException;

	public Executor update(Executor oldExecutor, Executor newExecutor) throws InternalApplicationException, ExecutorAlreadyExistsException, ExecutorOutOfDateException;

	public Executor[] getAll(BatchPresentation batchPresentation) throws InternalApplicationException;

	public Actor[] getAllActors(BatchPresentation batchPresentation) throws InternalApplicationException;

	public Group[] getAllGroups(BatchPresentation batchPresentation) throws InternalApplicationException;

	public void addExecutorsToGroup(Executor[] executors, Group group) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorAlreadyInGroupException;

	public void addExecutorToGroups(Executor executor, Group[] groups) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorAlreadyInGroupException;

	public void removeExecutorsFromGroup(Executor[] executors, Group group) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorNotInGroupException;

	public void removeExecutorFromGroups(Executor executor, Group[] groups) throws InternalApplicationException, ExecutorOutOfDateException, ExecutorNotInGroupException;

	/**
	 * Returns true if executor belongs to group recursively or false in any other case.
	 * <p>
	 * For example G1 contains G2, G2 contins A1. In this case:
	 * </p>
	 * <code>isExecutorInGroup(A1,G2) == true;</code>
	 * 
	 * @param executor an executor
	 * @param group a group
	 * @return true if executor belongs to group recursively or false in any other case.
	 * @throws ExecutorInternalApplicationException
	 */
	public boolean isExecutorInGroup(Executor executor, Group group) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns an array of group children.
	 * <p>
	 * For example G1 contains G2, G2 contins A1 and A2. In this case:
	 * </p>
	 * <code> getGroupChildren(G2) returns {A1, A2}</code>;<code> getGroupChildren(G1) returns {G2} </code>
	 * 
	 * @param group a group
	 * @param batchPresentation as Presentation of array returned
	 * @return an array of group children.
	 * @throws ExecutorInternalApplicationException
	 */
	public Executor[] getGroupChildren(Group group, BatchPresentation batchPresentation) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns an array of {@link Executor}that can be added to group
	 * 
	 * @param group
	 * @return
	 * @throws ExecutorInternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	public Executor[] getExecutorsCanBeAddedToGroup(Group group, BatchPresentation batchPresentation) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns an array of executor groups.
	 * 
	 * @param executor an executor
	 * @return an array of groups.
	 * @throws ExecutorInternalApplicationException
	 */
	public Group[] getExecutorGroups(Executor executor, BatchPresentation batchPresentation) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * sets permissions for executor on securedObject.
	 * 
	 * @param executor an executor
	 * @param permissions an array of permsissions
	 * @param securedObject an securedObject
	 * @throws ExecutorInternalApplicationException if Executor or Object doesn't exists.
	 */
	public void setPermissions(Executor executor, Permission[] permissions, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException, UnapplicablePermissionException;

	public void setPermissions(Executor[] executors, Permission[] permissions, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException, UnapplicablePermissionException;

	public void setPermissions(Executor[] executors, Permission[][] permissions, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException, UnapplicablePermissionException;

	/**
	 * Checks whether executor has permission on securedObject.
	 * 
	 * @param executor an executor
	 * @param permission an array of permsissions
	 * @param securedObject an securedObject
	 * @return true if executor has such permission on secuedObject
	 * @throws ExecutorInternalApplicationException if Executor or Object doesn't exists.
	 */
	public boolean isAllowed(Executor executor, Permission permission, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException;

	public boolean[] isAllowed(Executor executor, Permission permission, SecuredObject[] securedObjects) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException;

	/**
	 * Returns an array of Permission that executor has on securedObject. Returns as own permissions on securedObject as inherited group(s) permissions on securedObject.
	 * 
	 * @param executor an executor
	 * @param securedObject an securedObject
	 * @return an array of Permission
	 * @throws ExecutorInternalApplicationException if Executor or Object doesn't exists.
	 */
	public Permission[] getPermissions(Executor executor, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException;

	/**
	 * Returns an array of Permission that executor itself has on securedObject. Inherited permissions are not returned.
	 * 
	 * @param executor an executor
	 * @param securedObject an securedObject
	 * @return an array of Permission
	 * @throws ExecutorInternalApplicationException if Executor or Object doesn't exists. *
	 */
	public Permission[] getOwnPermissions(Executor executor, SecuredObject securedObject) throws InternalApplicationException, ExecutorOutOfDateException, SecuredObjectOutOfDateException;

	/**
	 * Returns Actor by name.
	 * 
	 * @param name of an Actor
	 * @return Actor with specified name
	 * @throws ExecutorOutOfDateException if something goes wrong, for example Actor with specified name does not exist
	 */
	public Actor getActor(String name) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Actors by array of executor ids. If id element belongs to group it is replaced by all actors in grour recursivly
	 * 
	 * @param executorIds executorIds
	 * @return Actor[]
	 * @throws ExecutorOutOfDateException if something goes wrong, for example Actor with specified id does not exist
	 */
	public Actor[] getActorsByExecutorIds(long[] executorIds) throws InternalApplicationException, ExecutorOutOfDateException;

	
	/**
	 * @param codes
	 * @return actor codes with probably different order
	 * @throws InternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	public long[] getActorsIdByCodes(long[] codes) throws InternalApplicationException, ExecutorOutOfDateException;
	
	/**
	 * Returns actor ids which exists in DB, other codes are ignored
	 * @throws InternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	public long[] getAvailableActorsIdByCodes(long[] codes) throws InternalApplicationException;

	/**
	 * Returns Actor for name without case check. This method is a big shame for us. It should never have its way out of DAO! It only purpose is to use with stupid Microsoft Active Directory authentication, which is case insensitive. <b>Never use it! </b>
	 * 
	 * @deprecated
	 * @param name
	 * @return
	 * @throws InternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	public Actor getActorCaseInsensitive(String name) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Actor by id.
	 * 
	 * @param id of an Actor
	 * @return Actor with specified id
	 * @throws ExecutorOutOfDateException if something goes wrong, for example Actor with specified name does not exist
	 */
	public Actor getActor(long id) throws InternalApplicationException, ExecutorOutOfDateException;

	public Actor[] getActors(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Actor[] by array of codes
	 * 
	 * @param codes
	 * @return
	 * @throws InternalApplicationException
	 * @throws ExecutorOutOfDateException
	 */
	public Actor[] getActorsByCodes(long[] codes) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Executor by name.
	 * 
	 * @param name of an Executor
	 * @return Executor with specified name
	 * @throws ExecutorOutOfDateException if something goes wrong, for example Executor with specified name does not exist
	 */
	public Executor getExecutor(String name) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Executor by id.
	 * 
	 * @param id of an Executor
	 * @return Executor with specified id
	 * @throws ExecutorOutOfDateException if Executor with specified id does not exist
	 */
	public Executor getExecutor(long id) throws InternalApplicationException, ExecutorOutOfDateException;

	public Executor[] getExecutors(long[] executorIds) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Group by name.
	 * 
	 * @param name of an Group
	 * @return Group with specified name
	 * @throws ExecutorInternalApplicationException
	 * @throws ExecutorOutOfDateException if Group with specified name does not exist
	 */
	public Group getGroup(String name) throws InternalApplicationException, ExecutorOutOfDateException;

	/**
	 * Returns Group by id.
	 * 
	 * @param id of an Group
	 * @return Group with specified id
	 * @throws ExecutorInternalApplicationException
	 * @throws ExecutorOutOfDateException if Group with specified id does not exist
	 */
	public Group getGroup(long id) throws InternalApplicationException, ExecutorOutOfDateException;

	public Group[] getGroups(long[] ids) throws InternalApplicationException, ExecutorOutOfDateException;

	public void setPassword(Actor actor, String password) throws InternalApplicationException, ExecutorOutOfDateException;
	
	public void setStatus(Actor actor, boolean isActive) throws InternalApplicationException, ExecutorOutOfDateException;

	public boolean isPasswordValid(Actor actor, String password) throws InternalApplicationException, ExecutorOutOfDateException;

	public Actor getActorByCode(long code) throws InternalApplicationException, ExecutorOutOfDateException;
	
	public boolean isActorExist(long code) throws InternalApplicationException;
	
	public long[] getActorAndGroupsIds(Actor actor);
	
	public boolean isActorActive(long code) throws InternalApplicationException, ExecutorOutOfDateException;
	
	public long[] getNotActiveActorCodes() throws InternalApplicationException;
	
}
