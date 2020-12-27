package org.jbpm.ui.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jbpm.ui.dialog.BSHEditorDialog;
import org.jbpm.ui.editor.DesignerEditor;
import org.jbpm.ui.model.Node;
import org.jbpm.ui.model.ProcessDefinition;
import org.jbpm.ui.model.Transition;
import org.jbpm.ui.model.Variable;
import org.jbpm.ui.util.ProjectFinder;

public class BSHCellEditor extends DialogCellEditor {

    private Node node;

    public BSHCellEditor(Composite parent, Node node) {
        super(parent, SWT.NONE);
        this.node = node;
    }

    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        String code = (String) getValue();
        BSHEditorDialog dialog = new BSHEditorDialog(cellEditorWindow.getShell(), code);
        
        List<Transition> transitions = node.getLeavingTransitions();
        List<String> transitionNames = new ArrayList<String>();
        for (Transition transition : transitions) {
			transitionNames.add(transition.getName());
		}
        dialog.setTransitionNames(transitionNames);

        ProcessDefinition processDefinition = ((DesignerEditor) ProjectFinder.getActiveEditor()).getProcessDefinition();
        dialog.setVariables(new ArrayList<Variable>(processDefinition.getVariables()));
        
        if(dialog.open() == Dialog.OK){
            return dialog.getResult();
        }
        return null;
    }
}
