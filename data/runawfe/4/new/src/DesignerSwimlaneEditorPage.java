package org.jbpm.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.jbpm.ui.command.ProcessDefinitionAddSwimlaneCommand;
import org.jbpm.ui.command.ProcessDefinitionRemoveSwimlaneCommand;
import org.jbpm.ui.model.ProcessDefinition;
import org.jbpm.ui.model.Swimlane;
import org.jbpm.ui.resource.Messages;
import org.jbpm.ui.wizard.NewSwimlaneWizard;

public class DesignerSwimlaneEditorPage extends EditorPart implements PropertyChangeListener {

	private static final String DEFAULT_LABEL = Messages.getString("DesignerSwimlaneEditorPage.label.default_text"); //$NON-NLS-1$

	private final DesignerEditor editor;

	private Label descriptionLabel;

	private ListViewer viewer;

	public DesignerSwimlaneEditorPage(DesignerEditor editor) {
		this.editor = editor;
	}

	@Override
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		ScrolledForm form = toolkit.createScrolledForm(parent);
		form.setText(Messages.getString("DesignerSwimlaneEditorPage.label.swimlanes")); //$NON-NLS-1$

		GridLayout layout1 = new GridLayout();
		layout1.marginWidth = 0;
		layout1.marginHeight = 0;
		form.getBody().setLayout(layout1);

		SashForm sashForm = new SashForm(form.getBody(), SWT.NULL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(form.getBody().getMenu());
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		Section master = toolkit.createSection(sashForm, Section.TITLE_BAR);
		master.marginHeight = 5;
		master.marginWidth = 5;
		master.setText(Messages.getString("DesignerSwimlaneEditorPage.label.all_swimlanes")); //$NON-NLS-1$

		Composite masterClient = toolkit.createComposite(master);
		master.setClient(masterClient);
		toolkit.paintBordersFor(masterClient);

		GridLayout layout3 = new GridLayout();
		layout3.marginWidth = 2;
		layout3.marginHeight = 2;
		layout3.numColumns = 2;
		masterClient.setLayout(layout3);

		viewer = new ListViewer(masterClient);

		toolkit.adapt(viewer.getControl(), false, false);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		viewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((Swimlane) element).getName();
			}
		});
		viewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				List list = (List) inputElement;
				return list.toArray(new Object[list.size()]);
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) ((ListViewer) event.getSource()).getSelection();
				Swimlane swimlane = ((Swimlane) selection.getFirstElement());
				setDescriptionLabel(swimlane);
			}

		});

		fillViewer();
		getSite().setSelectionProvider(viewer);

		editor.getProcessDefinition().addPropertyChangeListener(this);

		addButtons(toolkit, masterClient);

		Section detail = toolkit.createSection(sashForm, Section.TITLE_BAR);
		detail.marginHeight = 5;
		detail.marginWidth = 5;
		detail.setText(Messages.getString("DesignerSwimlaneEditorPage.label.swimlane_details")); //$NON-NLS-1$

		Composite detailClient = toolkit.createComposite(detail);
		detail.setClient(detailClient);

		GridLayout layout4 = new GridLayout();
		layout4.marginWidth = 2;
		layout4.marginHeight = 2;
		layout4.numColumns = 2;
		detailClient.setLayout(layout4);

		descriptionLabel = toolkit.createLabel(detailClient, DEFAULT_LABEL, SWT.WRAP);

	}

	private void addButtons(FormToolkit toolkit, Composite masterClient) {
		final Shell shell = masterClient.getShell();
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.verticalAlignment = SWT.TOP;

		Button addButton = toolkit.createButton(masterClient, Messages.getString("DesignerSwimlaneEditorPage.label.add"), SWT.PUSH); //$NON-NLS-1$
		addButton.setLayoutData(gridData);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				NewSwimlaneWizard wizard = new NewSwimlaneWizard();
				wizard.setWindowTitle(Messages.getString("Swimlane.wizard.title"));
				WizardDialog dialog = new WizardDialog(shell, wizard);
				if (dialog.open() != Window.CANCEL) {
					ProcessDefinitionAddSwimlaneCommand command = new ProcessDefinitionAddSwimlaneCommand();
					String swimlaneName;
					if ((wizard.getSwimlaneName() != null) || !("".equals(wizard.getSwimlaneName())))
						swimlaneName = wizard.getSwimlaneName();
					else
						swimlaneName = editor.getProcessDefinition().getNextSwimlaneName();
					command.setName(swimlaneName);
					if (wizard.getSwimlaneDescription() != null || !("".equals(wizard.getSwimlaneDescription())))
						command.setDescription(wizard.getSwimlaneDescription());
					command.setProcessDefinition(editor.getProcessDefinition());
					String configuration = createSwimlaneConfiguration(swimlaneName, wizard.getType(), wizard.getParams(), wizard.isAddReplacement());
					command.setConfiguration(configuration);
					editor.getCommandStack().execute(command);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button removeButton = toolkit.createButton(masterClient, Messages.getString("DesignerSwimlaneEditorPage.label.remove"), SWT.PUSH); //$NON-NLS-1$
		removeButton.setLayoutData(gridData);
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					Swimlane swimlane = (Swimlane) iter.next();
					ProcessDefinitionRemoveSwimlaneCommand command = new ProcessDefinitionRemoveSwimlaneCommand();
					command.setProcessDefinition(editor.getProcessDefinition());
					command.setSwimlane(swimlane);
					editor.getCommandStack().execute(command);
					descriptionLabel.setText(DEFAULT_LABEL);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void fillViewer() {
		List<Swimlane> old = (List<Swimlane>) viewer.getInput();
		if (old != null) {
			for (Swimlane swimlane : old) {
				swimlane.removePropertyChangeListener(this);
			}
		}
		for (Swimlane swimlane : editor.getProcessDefinition().getSwimlanes()) {
			swimlane.addPropertyChangeListener(this);
		}
		viewer.setInput(editor.getProcessDefinition().getSwimlanes());
	}

	private void setDescriptionLabel(Swimlane swimlane) {
		String text = swimlane.getDescription();
		descriptionLabel.setText(text == null ? "" : text);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
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
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String type = evt.getPropertyName();
		if (ProcessDefinition.ELEMENT_SWIMLANE_ADDED.equals(type) || ProcessDefinition.ELEMENT_SWIMLANE_REMOVED.equals(type)) {
			fillViewer();
		} else if (evt.getSource() instanceof Swimlane && Swimlane.PROPERTY_NAME.equals(type)) {
			viewer.refresh(evt.getSource());
		} else if (evt.getSource() instanceof Swimlane && Swimlane.PROPERTY_DESCRIPTION.equals(type)) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			if (evt.getSource().equals(selection.getFirstElement())) {
				setDescriptionLabel((Swimlane) evt.getSource());
			}
		}
	}

	private String createSwimlaneConfiguration(String swimlaneName, String typeName, List params, boolean addReplacement) {
		String result = "";
		if (typeName != null) {
			result = typeName + "(";
			if (params != null) {
				for (Iterator iter = params.iterator(); iter.hasNext();) {
					String param = (String) iter.next();
					result += param;
					if (iter.hasNext())
						result += ", ";
				}
			}
			result += ")";
		}
		if (addReplacement) {
			if(!"".equals(result))
				result += ";";
			result += "ru.runa.af.organizationfunction.TableSubstitutionFunction(?," + editor.getProcessDefinition().getName() + "." + swimlaneName + "):"
					+ "ru.runa.af.organizationfunction.IfNotActiveSubstitutionCriteria(?)";

		}
		return result;
	}
}
