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
package ru.runa.common.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

import ru.runa.af.AuthenticationException;
import ru.runa.af.InternalApplicationException;
import ru.runa.af.delegate.DelegateFactory;
import ru.runa.af.delegate.ProfileServiceDelegate;
import ru.runa.af.presentation.BatchPresentation;
import ru.runa.af.presentation.BatchPresentationNotFoundException;
import ru.runa.af.presentation.Profile;
import ru.runa.af.presentation.filter.FilterCriteria;
import ru.runa.af.presentation.filter.FilterFormatException;
import ru.runa.af.web.SubjectHttpSessionHelper;
import ru.runa.common.web.ActionExceptionHelper;
import ru.runa.common.web.Messages;
import ru.runa.common.web.ProfileHttpSessionHelper;
import ru.runa.common.web.form.TableViewSetupForm;
import ru.runa.common.web.html.format.FilterFormatsFactory;
import ru.runa.commons.ArgumentsCommons;

/**
 * Created on 26.01.2005
 * 
 * @author Vitaliy S aka Yilativs<a href="mailto:runawfe@gmail.com">
 * @author Gordienko_m@runa.ru
 * @struts:action path="/tableViewSetup" name="tableViewSetupForm" validate="false" parameter = "dispatch"
 */
public class TableViewSetupFormAction extends LookupDispatchAction {

	public static final String ACTION_PATH = "/tableViewSetup";

	public static final String PARAMETER_NAME = "dispatch";

	protected Map getKeyMethodMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(Messages.BUTTON_APPLY, "apply");
		map.put(Messages.BUTTON_SAVE, "save");
		map.put(Messages.BUTTON_SAVE_AS, "saveAs");
		map.put(Messages.BUTTON_REMOVE, "delete");
		return map;
	}

	public ActionForward apply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		TableViewSetupForm tableViewSetupForm = (TableViewSetupForm) form;
		Profile profile = ProfileHttpSessionHelper.getProfile(request.getSession());
		try {
			BatchPresentation batchPresentation = getActiveBatchPresentation(profile, tableViewSetupForm.getBatchPresentationId());
			applyBatchPresentation(batchPresentation, tableViewSetupForm, errors);
		} catch (BatchPresentationNotFoundException e) {
			ActionExceptionHelper.addException(errors, e);
		}
		if (!errors.isEmpty()) {
			saveErrors(request.getSession(), errors);
		}
		return new ActionForward(tableViewSetupForm.getReturnAction(),true);
	}

	private void applyBatchPresentation(BatchPresentation batchPresentation, TableViewSetupForm tableViewSetupForm, ActionMessages errors) {
		batchPresentation.setFieldsToDisplayIds(tableViewSetupForm.getDisplayPositionsIds());
		batchPresentation.setFieldsToSort(tableViewSetupForm.getSortPositionsIds(), tableViewSetupForm.getSortingModes());
		try {
			Map<Integer,FilterCriteria> result = FilterFormatsFactory.getParser().parse(batchPresentation, tableViewSetupForm.getFieldsToFilterCriteriasMap());
			batchPresentation.setFilteredFieldsMap(result);
			batchPresentation.setFieldsToGroup(tableViewSetupForm.getFieldsToGroupIds());
			batchPresentation.setPredefinedRangeSize(tableViewSetupForm.getViewSize());
			batchPresentation.setExternalCriteriaFilter(tableViewSetupForm.getInstanceVariableFilterCondition());
		} catch (FilterFormatException e) {
			ActionExceptionHelper.addException(errors, e);
		}
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		apply(mapping, form, request, response);
		TableViewSetupForm tableViewSetupForm = (TableViewSetupForm) form;
		Profile profile = ProfileHttpSessionHelper.getProfile(request.getSession());
		saveProfile(request, profile, tableViewSetupForm);
		return new ActionForward(tableViewSetupForm.getReturnAction(),true);
	}

	private void saveProfile(HttpServletRequest request, Profile profile, TableViewSetupForm tableViewSetupForm) throws AuthenticationException {
		ProfileServiceDelegate profileServiceDelegate = DelegateFactory.getInstance().getProfileServiceDelegate();
		Subject subject = SubjectHttpSessionHelper.getActorSubject(request.getSession());
//		comment following line for WFDEMO
		profileServiceDelegate.saveProfile(subject, profile);
		profile = profileServiceDelegate.getProfile(subject); /* next 2 lines used to reloading profile from db */
		profile.changeBlockVisibility(tableViewSetupForm.getBatchPresentationId());
		ProfileHttpSessionHelper.setProfile(profile, request.getSession());
	}

	private static final String DEFAUL_VIEW_SETUP_NAME = "";
	public ActionForward saveAs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InternalApplicationException, AuthenticationException {
		ActionMessages errors = new ActionMessages();
		TableViewSetupForm tableViewSetupForm = (TableViewSetupForm) form;
		Profile profile = ProfileHttpSessionHelper.getProfile(request.getSession());
		try {
			BatchPresentation presentation = getActiveBatchPresentation(profile, tableViewSetupForm.getBatchPresentationId());
			String newName = (String)ArgumentsCommons.setToDefaultIfNull(tableViewSetupForm.getSaveAsBatchPresentationName(),DEFAUL_VIEW_SETUP_NAME);
			BatchPresentation batchPresentationClone = (BatchPresentation) presentation.clone();
			batchPresentationClone.setBatchPresentationName(newName);
			applyBatchPresentation(batchPresentationClone, tableViewSetupForm, errors);
			profile.addBatchPresentation(batchPresentationClone);
			profile.setActiveBatchPresentation(batchPresentationClone.getBatchPresentationId(), batchPresentationClone.getBatchPresentationName());
			saveProfile(request, profile, tableViewSetupForm);//profile.getActiveBatchPresentation(tableViewSetupForm.getBatchPresentationId())
		} catch (BatchPresentationNotFoundException e) {
			log.error(e);
			ActionExceptionHelper.addException(errors, e);
		}
		if (!errors.isEmpty()) {
			saveErrors(request.getSession(), errors);
		}
		return new ActionForward(tableViewSetupForm.getReturnAction(),true);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InternalApplicationException, AuthenticationException {
		ActionMessages errors = new ActionMessages();
		TableViewSetupForm tableViewSetupForm = (TableViewSetupForm) form;
		Profile profile = ProfileHttpSessionHelper.getProfile(request.getSession());
		try {
			BatchPresentation presentation = getActiveBatchPresentation(profile, tableViewSetupForm.getBatchPresentationId());
			profile.deleteBatchPresentation(presentation);
			saveProfile(request, profile, tableViewSetupForm);
		} catch (BatchPresentationNotFoundException e) {
			log.error(e);
			ActionExceptionHelper.addException(errors, e);
		}
		if (!errors.isEmpty()) {
			saveErrors(request.getSession(), errors);
		}
		return new ActionForward(tableViewSetupForm.getReturnAction(),true);
	}

	private BatchPresentation getActiveBatchPresentation(Profile profile, String batchPresentationId) throws BatchPresentationNotFoundException {
		return profile.getActiveBatchPresentation(batchPresentationId);
	}
}

