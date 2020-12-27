package org.jbpm.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.jbpm.ui.SharedImages;
import org.jbpm.ui.command.ProcessDefinitionAddVariablesToStateCommand;
import org.jbpm.ui.command.ProcessDefinitionCreateVariableCommand;
import org.jbpm.ui.command.ProcessDefinitionExcludeVariablesFromStateCommand;
import org.jbpm.ui.command.ProcessDefinitionMoveStateVariablesCommand;
import org.jbpm.ui.command.ProcessDefinitionMoveVariablesCommand;
import org.jbpm.ui.command.ProcessDefinitionRemoveVariablesCommand;
import org.jbpm.ui.dialog.CreateVariableDialog;
import org.jbpm.ui.model.Node;
import org.jbpm.ui.model.ProcessDefinition;
import org.jbpm.ui.model.StateVariableProperties;
import org.jbpm.ui.model.Variable;
import org.jbpm.ui.model.VariableNode;
import org.jbpm.ui.resource.Messages;

public class DesignerVariableEditorPage extends EditorPart implements
		PropertyChangeListener {

	private DesignerEditor editor;

	private TableViewer stateVariableList;

	private TableViewer variableList;

	private TableViewer stateList;

	Table stateTable;

	public DesignerVariableEditorPage(DesignerEditor editor) {
		this.editor = editor;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		editor.getProcessDefinition().addPropertyChangeListener(this);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		ScrolledForm form = toolkit.createScrolledForm(parent);
		form.setText(Messages
				.getString("DesignerVariableEditorPage.label.variables")); 
		form.getBody().setLayout(new GridLayout());
		SashForm sashForm = new SashForm(form.getBody(), SWT.NULL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(form.getBody().getMenu());
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		Section allVariableSelection = toolkit.createSection(sashForm,
				Section.TITLE_BAR);
		allVariableSelection.setText(Messages
				.getString("DesignerVariableEditorPage.label.all_variables")); 
		Composite allVariableClientComposite = toolkit
				.createComposite(allVariableSelection);
		allVariableSelection.setClient(allVariableClientComposite);
		toolkit.paintBordersFor(allVariableClientComposite);
		allVariableClientComposite.setLayout(new GridLayout(1, false));
		allVariableSelection.setClient(allVariableClientComposite);
		Composite allVariablesListComposite = toolkit
				.createComposite(allVariableClientComposite);
		allVariablesListComposite
				.setLayoutData(new GridData(GridData.FILL_BOTH));
		allVariablesListComposite.setLayout(new GridLayout(2, false));
		createVariableList(toolkit, allVariablesListComposite);
		Composite businessButtonMoveComposite = toolkit.createComposite(
				allVariablesListComposite);
		businessButtonMoveComposite.setLayoutData(new GridData(
				GridData.FILL_BOTH));
		businessButtonMoveComposite.setLayout(new GridLayout(1, false));
		Composite buttonComposite1 = toolkit.createComposite(
				businessButtonMoveComposite);
		buttonComposite1.setLayoutData(
				new GridData(SWT.LEFT, SWT.TOP, true, true));
		buttonComposite1.setLayout(new GridLayout(1, false));
		addButton(toolkit, buttonComposite1, Messages
				.getString("DesignerVariableEditorPage.label.up"),
				new MoveVariableSelectionListener(true), "/icons/up.gif",
				SWT.TOP);
		addButton(toolkit, buttonComposite1, Messages
				.getString("DesignerVariableEditorPage.label.down"),
				new MoveVariableSelectionListener(false), "/icons/down.gif",
				SWT.TOP);
		Composite buttonComposite2 = toolkit.createComposite(
				businessButtonMoveComposite);
		buttonComposite2.setLayoutData(
				new GridData(SWT.LEFT, SWT.TOP, true, true));
		buttonComposite2.setLayout(new GridLayout(1, false));
		addButton(
				toolkit,
				buttonComposite2,
				Messages.getString("DesignerVariableEditorPage.label.add"), new AddVariableSelectionListener(),
				"/icons/right.gif", SWT.CENTER);
		addButton(
				toolkit,
				buttonComposite2,
				Messages.getString("DesignerVariableEditorPage.label.exclude"), new ExcludeVariableSelectionListener(), 
				"/icons/left.gif", SWT.CENTER);
		Composite businessButtonComposite = toolkit
				.createComposite(allVariableClientComposite);
		businessButtonComposite.setLayout(new GridLayout(2, false));
		addButton(
				toolkit,
				businessButtonComposite,
				Messages.getString("DesignerVariableEditorPage.label.create"), new CreateVariableSelectionListener(parent)); 
		addButton(
				toolkit,
				businessButtonComposite,
				Messages.getString("DesignerVariableEditorPage.label.remove"), new RemoveVariableSelectionListener()); 
		Section stateVariableSelection = toolkit.createSection(sashForm,
				Section.TITLE_BAR);
		stateVariableSelection.setText(Messages
				.getString("DesignerVariableEditorPage.label.state_variables")); 
		Composite stateVariableComposite = toolkit
				.createComposite(stateVariableSelection);
		stateVariableSelection.setClient(stateVariableComposite);
		toolkit.paintBordersFor(stateVariableComposite);
		stateVariableComposite.setLayout(new GridLayout(2, false));
		createStateVariableList(toolkit, stateVariableComposite);
		Composite stateButtonMoveComposite = toolkit
				.createComposite(stateVariableComposite);
		stateButtonMoveComposite
				.setLayoutData(new GridData(GridData.FILL_BOTH));
		stateButtonMoveComposite.setLayout(new GridLayout(1, true));
		addButton(toolkit, stateButtonMoveComposite, Messages
				.getString("DesignerVariableEditorPage.label.up"),
				new MoveStateVariableSelectionListener(true), "/icons/up.gif",
				SWT.TOP);
		addButton(toolkit, stateButtonMoveComposite, Messages
				.getString("DesignerVariableEditorPage.label.down"),
				new MoveStateVariableSelectionListener(false),
				"/icons/down.gif", SWT.TOP);

		Section stateSelection = toolkit.createSection(sashForm,
				Section.TITLE_BAR);
		stateSelection.setText(Messages
				.getString("DesignerVariableEditorPage.label.states")); 
		Composite stateComposite = toolkit.createComposite(stateSelection);
		stateSelection.setClient(stateComposite);
		toolkit.paintBordersFor(stateComposite);
		stateComposite.setLayout(new GridLayout(1, false));
		createStateList(toolkit, stateComposite);

	}

	private void createStateList(FormToolkit toolkit, Composite masterClient) {
		stateTable = new Table(masterClient, SWT.SINGLE | SWT.READ_ONLY);
		stateList = new TableViewer(stateTable);
		toolkit.adapt(stateList.getControl(), false, false);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.minimumWidth = 100;
		stateList.getControl().setLayoutData(gridData);
		stateList.getControl().addFocusListener(
				new TableFocusListener(stateList));

		stateList.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((VariableNode) element).getName();
			}
			
		});

		stateList.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				List list = (List) inputElement;
				return list.toArray(new Object[list.size()]);
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});

		stateList.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) ((TableViewer) event
						.getSource()).getSelection();
				VariableNode formNode = (VariableNode) selection
						.getFirstElement();
				stateVariableList.setInput(formNode
						.getStateVariablePropertiesList());
			}

		});
	}

	public void selectNode(VariableNode variableNode) {
		stateList.setSelection(new StructuredSelection(variableNode));
	}

	private void createVariableList(FormToolkit toolkit, Composite masterClient) {
		variableList = new TableViewer(masterClient);
		toolkit.adapt(variableList.getControl(), false, false);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.minimumWidth = 100;
		variableList.getControl().setLayoutData(gridData);
		variableList.getControl().addFocusListener(
				new TableFocusListener(variableList));

		variableList.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Variable) element).getName();
			}
		});

		variableList
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						showState(true);
					}

				});

		variableList.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				List<Variable> list = (ArrayList<Variable>) inputElement;
				return list.toArray(new Variable[list.size()]);
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
	}

	private void createStateVariableList(FormToolkit toolkit,
			Composite masterClient) {
		stateVariableList = new TableViewer(masterClient);
		toolkit.adapt(stateVariableList.getControl(), false, false);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.minimumWidth = 100;
		stateVariableList.getControl().setLayoutData(gridData);
		stateVariableList.getControl().addFocusListener(
				new TableFocusListener(stateVariableList));

		stateVariableList.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((StateVariableProperties) element).getVariable()
						.getName();
			}
		});

		stateVariableList
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						showState(false);
					}

				});
		stateVariableList.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				Collection list = (Collection) inputElement;
				return list.toArray(new Object[list.size()]);
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
	}

	private void addButton(FormToolkit toolkit, Composite masterClient,
			String buttonName, SelectionListener selectionListener) {
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		Button button = toolkit
				.createButton(masterClient, buttonName, SWT.PUSH);
		button.setLayoutData(gridData);
		button.addSelectionListener(selectionListener);
	}

	private void addButton(FormToolkit toolkit, Composite masterClient,
			String buttonName, SelectionListener selectionListener,
			String image, int vert) {
		Button button = toolkit
				.createButton(masterClient, buttonName, SWT.PUSH);
		button.setImage(SharedImages.getImage(SharedImages
				.getImageDescriptor(image)));
		button.setLayoutData(new GridData(SWT.RIGHT, vert, false, false));
		button.addSelectionListener(selectionListener);
	}

	private List<VariableNode> getStateList() {
		List<VariableNode> stateList = new ArrayList<VariableNode>();
		for (Node node : editor.getProcessDefinition().getNodes()) {
			if (node instanceof VariableNode) {
				VariableNode formNode = (VariableNode) node;
				stateList.add(formNode);
			}
		}
		return stateList;
	}

	private List<VariableNode> getVariableStateList(
			StateVariableProperties stateVariableProperties) {
		List<VariableNode> stateList = new ArrayList<VariableNode>();
		for (Node node : editor.getProcessDefinition().getNodes()) {
			if (node instanceof VariableNode) {
				VariableNode formNode = (VariableNode) node;
				if (formNode.getStateVariablePropertiesList().contains(
						stateVariableProperties))
					stateList.add(formNode);
			}
		}
		return stateList;
	}

	@Override
	public void setFocus() {
		updateStateList();
		updateVariableLists();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String type = evt.getPropertyName();
		if (ProcessDefinition.ELEMENT_VARIABLE_ADDED.equals(type)
				|| ProcessDefinition.ELEMENT_VARIABLE_REMOVED.equals(type)
				|| ProcessDefinition.ELEMENT_VARIABLE_MOVED.equals(type)) {
			updateVariableLists();
		} else if (ProcessDefinition.PROCESS_DEFINITION_NODE_ADDED.equals(type)
				|| ProcessDefinition.PROCESS_DEFINITION_NODE_REMOVED
						.equals(type)) {
			updateStateList();
		} else if (ProcessDefinition.PROPERTY_NAME.equals(type)
				&& evt.getSource() instanceof Variable) {
			variableList.refresh(evt.getSource());
			stateVariableList.refresh();
		} else if (ProcessDefinition.PROPERTY_NAME.equals(type)
				&& evt.getSource() instanceof VariableNode) {
			stateList.refresh(evt.getSource());
		}

	}

	private void updateVariableLists() {
		List<Variable> variables = editor.getProcessDefinition().getVariables();
		variableList.setInput(variables);
		for (Iterator iter = variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.addPropertyChangeListener(this);
		}
		IStructuredSelection selection = (IStructuredSelection) stateList
				.getSelection();
		VariableNode formNode = (VariableNode) selection.getFirstElement();
		if (formNode != null) {
			stateVariableList.setInput(formNode
					.getStateVariablePropertiesList());
		}
	}

	private void updateStateList() {
		stateList.setInput(getStateList());
	}

	private final class TableFocusListener implements FocusListener {
		private final TableViewer viewer;

		public TableFocusListener(TableViewer viewer) {
			this.viewer = viewer;
		}

		public void focusGained(FocusEvent e) {
			getSite().setSelectionProvider(viewer);

		}

		public void focusLost(FocusEvent e) {
			getSite().setSelectionProvider(null);
		}
	}

	private class AddVariableSelectionListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {

		}

		public void widgetSelected(SelectionEvent e) {
			ProcessDefinitionAddVariablesToStateCommand command = new ProcessDefinitionAddVariablesToStateCommand();
			IStructuredSelection selection = (IStructuredSelection) stateList
					.getSelection();
			if (!selection.isEmpty()) {
				VariableNode formMode = (VariableNode) selection
						.getFirstElement();
				command.setVariableNode(formMode);
				IStructuredSelection variableSelection = (IStructuredSelection) variableList
						.getSelection();
				if (!variableSelection.isEmpty()) {
					Object[] objects = variableSelection.toArray();
					StateVariableProperties[] stateVariableProperties = new StateVariableProperties[objects.length];
					for (int i = 0; i < objects.length; i++) {
						stateVariableProperties[i] = new StateVariableProperties(
								(Variable) objects[i], true, true, true, editor
										.getProcessDefinition());
					}
					command.setStateVariableProperties(stateVariableProperties);
					editor.getCommandStack().execute(command);
					IStructuredSelection selection2 = new StructuredSelection(
							stateVariableProperties);
					stateVariableList.setSelection(selection2);
				}
			}
		}
	}

	private class ExcludeVariableSelectionListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {

		}

		public void widgetSelected(SelectionEvent e) {
			ProcessDefinitionExcludeVariablesFromStateCommand command = new ProcessDefinitionExcludeVariablesFromStateCommand();
			IStructuredSelection selection = (IStructuredSelection) stateList
					.getSelection();
			if (!selection.isEmpty()) {
				VariableNode formMode = (VariableNode) selection
						.getFirstElement();
				command.setVariableNode(formMode);
				IStructuredSelection variableSelection = (IStructuredSelection) stateVariableList
						.getSelection();
				if (!variableSelection.isEmpty()) {
					Object[] objects = variableSelection.toArray();
					StateVariableProperties[] stateVariableProperties = new StateVariableProperties[objects.length];
					System.arraycopy(objects, 0, stateVariableProperties, 0,
							objects.length);
					command.setStateVariableProperties(stateVariableProperties);
					editor.getCommandStack().execute(command);
				}
			}
		}
	}

	private class RemoveVariableSelectionListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {

		}

		public void widgetSelected(SelectionEvent e) {
			ProcessDefinitionRemoveVariablesCommand command = new ProcessDefinitionRemoveVariablesCommand();
			IStructuredSelection selection = (IStructuredSelection) variableList
					.getSelection();
			if (!selection.isEmpty()) {
				Object[] objects = selection.toArray();
				Variable[] variables = new Variable[objects.length];
				System.arraycopy(objects, 0, variables, 0, objects.length);
				command.setProcessDefinition(editor.getProcessDefinition());
				command.setVariables(variables);
				editor.getCommandStack().execute(command);
			}
		}
	}

	private class CreateVariableSelectionListener implements SelectionListener {

		private Composite parent;

		public CreateVariableSelectionListener(Composite parent) {
			this.parent = parent;
		}

		public void widgetSelected(SelectionEvent e) {

			CreateVariableDialog dialog = new CreateVariableDialog(parent
					.getShell());
			if (dialog.open() == Window.OK) {
				ProcessDefinitionCreateVariableCommand command = new ProcessDefinitionCreateVariableCommand();
				command.setProcessDefinition(editor.getProcessDefinition());
				Variable variable;
				String varName;
				String varType;
				if ((dialog.getName() == null) || ("".equals(dialog.getName()))) {
					varName = editor.getProcessDefinition()
							.getNextVariableName();
				} else {
					varName = dialog.getName();
				}
				varType = dialog.getType();
				variable = new Variable(varName, varName);
				variable.setFormat(varType);
				command.setVariable(variable);
				editor.getCommandStack().execute(command);
				IStructuredSelection selection = new StructuredSelection(
						variable);
				variableList.setSelection(selection);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}

	}

	private class MoveVariableSelectionListener implements SelectionListener {

		private boolean up;

		public MoveVariableSelectionListener(boolean up) {
			this.up = up;
		}

		public void widgetSelected(SelectionEvent e) {
			ProcessDefinitionMoveVariablesCommand command = new ProcessDefinitionMoveVariablesCommand(
					up);
			IStructuredSelection selection = (IStructuredSelection) variableList
					.getSelection();

			if (!selection.isEmpty()) {
				Variable variable = (Variable) selection.getFirstElement();
				command.setVariable(variable);
				command.setProcessDefinition(editor.getProcessDefinition());
				editor.getCommandStack().execute(command);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}

	}

	private class MoveStateVariableSelectionListener implements
			SelectionListener {

		private boolean up;

		public MoveStateVariableSelectionListener(boolean up) {
			this.up = up;
		}

		public void widgetSelected(SelectionEvent e) {
			ProcessDefinitionMoveStateVariablesCommand command = new ProcessDefinitionMoveStateVariablesCommand(
					up);
			IStructuredSelection selection = (IStructuredSelection) stateList
					.getSelection();
			if (!selection.isEmpty()) {
				VariableNode formMode = (VariableNode) selection
						.getFirstElement();
				command.setVariableNode(formMode);
				IStructuredSelection variableSelection = (IStructuredSelection) stateVariableList
						.getSelection();
				if (!variableSelection.isEmpty()) {
					StateVariableProperties stateVariableProperties = (StateVariableProperties) variableSelection
							.getFirstElement();
					command.setStateVariableProperties(stateVariableProperties);

					editor.getCommandStack().execute(command);
				}
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}

	}

	private void showState(boolean all) {
		IStructuredSelection variableSelection;
		StateVariableProperties stateVariableProperties;
		if (all) {
			variableSelection = (IStructuredSelection) variableList
					.getSelection();
			if (!variableSelection.isEmpty()) {
				Variable variable = (Variable) variableSelection
						.getFirstElement();
				stateVariableProperties = new StateVariableProperties(variable,
						true, true, true, editor.getProcessDefinition());
			} else
				return;
		} else {
			variableSelection = (IStructuredSelection) stateVariableList
					.getSelection();
			if (!variableSelection.isEmpty()) {
				stateVariableProperties = (StateVariableProperties) variableSelection
						.getFirstElement();
			} else
				return;
		}
		List stateVariableList = getVariableStateList(stateVariableProperties);
		if (stateVariableList != null) {
			for (int i = 0; i < stateTable.getItemCount(); i++) {
				TableItem tableItem = stateTable.getItem(i);
				if (stateVariableList.contains(tableItem.getData())) {
					tableItem.setImage(SharedImages.getImage(SharedImages
							.getImageDescriptor("/icons/plus.gif")));;
				} else {
					tableItem.setImage((Image)null);
				}
			}

		}
	}

}
