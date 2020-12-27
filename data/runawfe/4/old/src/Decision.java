package org.jbpm.ui.model;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jbpm.ui.contributor.GPDLayer;
import org.jbpm.ui.properties.ClassPropertyDescriptor;
import org.jbpm.ui.resource.Messages;

public class Decision extends DescribableNode implements InternalState, Delegable, Active {

	private final Active delegate;

	private final Delegable delegation;

	public Decision() {
		super();
		this.delegate = new DefaultStateActionElement(this);
		this.delegation = new DefaultDelegationElement(this);
	}

	public void addAction(Action action) {
		delegate.addAction(action);
	}

	public List<Action> getActions() {
		return delegate.getActions();
	}

	public void removeAction(Action action) {
		delegate.removeAction(action);
	}

	public String getConfiguration() {
		return delegation.getConfiguration();
	}

	public String getDelegateClassName() {
		return delegation.getDelegateClassName();
	}

	public void setConfiguration(String configuration) {
		delegation.setConfiguration(configuration);
	}

	public void setDelegateClassName(String delegateClassName) {
		delegation.setDelegateClassName(delegateClassName);
	}

	@Override
	public String getNamePrefix() {
		return "decision";
	}

	@Override
	public Dimension getInitialSize() {
		return new Dimension(GPDLayer.GRID_SIZE*4, GPDLayer.GRID_SIZE*2);
	}

	private static final IPropertyDescriptor[] DESCRIPTORS = {
			new TextPropertyDescriptor(PROPERTY_NAME, Messages.getString("Decision.property.name")), new ClassPropertyDescriptor(PROPERTY_CLASS, Messages.getString("Decision.property.class"), "org.jbpm.delegation.DecisionHandler"), new TextPropertyDescriptor(PROPERTY_CONFIGURATION, Messages.getString("Decision.property.configuration")) }; 
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return DESCRIPTORS;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_NAME.equals(id)) {
			String name = getName();
			return name == null ? "" : name;
		} else if (PROPERTY_CLASS.equals(id)) {
			String className = getDelegateClassName();
			return className == null ? "" : className;
		} else if (PROPERTY_CONFIGURATION.equals(id)) {
			String config = getConfiguration();
			return config == null ? "" : config;
		}
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (PROPERTY_NAME.equals(id)) {
			setName((String) value);
		} else if (PROPERTY_CLASS.equals(id)) {
			setDelegateClassName((String) value);
		} else if (PROPERTY_CONFIGURATION.equals(id)) {
			setConfiguration((String) value);
		}
	}

	@Override
	public boolean canAddLeavingTransition(org.jbpm.ui.model.Node target) {
		for (Transition leavingTransition : getLeavingTransitions()) {
			if (leavingTransition.getTarget().equals(target)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canAddArrivingTransition(org.jbpm.ui.model.Node source) {

		for (Transition arrivingTransition : getArrivingTransitions()) {
			if (arrivingTransition.getSource().equals(source)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canReconnectArrivingTransition(Transition transition, Node source) {
		return canAddArrivingTransition(source);
	}

	@Override
	public boolean canReconnectLeavingTransition(Transition transition, Node target) {
		return canAddLeavingTransition(target);
	}

}
