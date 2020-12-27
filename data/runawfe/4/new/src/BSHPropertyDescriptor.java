package org.jbpm.ui.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jbpm.ui.model.Node;

public class BSHPropertyDescriptor extends PropertyDescriptor {
	private Node node;
	
    public BSHPropertyDescriptor(Node node, Object id, String displayName) {
        super(id, displayName);
        this.node = node;
    }

    @Override
    public CellEditor createPropertyEditor(Composite parent) {
        return new BSHCellEditor(parent, node);
    }

}
