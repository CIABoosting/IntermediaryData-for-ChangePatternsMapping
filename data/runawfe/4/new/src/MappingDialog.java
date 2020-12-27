package org.jbpm.ui.dialog;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jbpm.ui.resource.Messages;
import org.jbpm.ui.util.TypeNameMapping;

public class MappingDialog extends Dialog {

	TableViewer tableViewer;

	public MappingDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1, false);
		area.setLayout(layout);
		createTableViewer(area);
		addButtons(area);

		return area;
	}

	private void createTableViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.minimumHeight = 300;
		tableViewer.getControl().setLayoutData(data);
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		String[] columnNames = new String[] { Messages.getString("Mapping.Type"), Messages.getString("Mapping.Name") };
		int[] columnWidths = new int[] { 400, 240 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}
		tableViewer.setLabelProvider(new MappingTableLabelProvider());
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				Map<String, String> map = (Map<String, String>) inputElement;
				Mapping[] array = new Mapping[map.size()];
				int i = 0;
				for (Iterator iter = map.keySet().iterator(); iter.hasNext(); i++) {
					String key = (String) iter.next();
					Mapping mapping = new Mapping();
					mapping.setType(key);
					mapping.setName(map.get(key));
					array[i] = mapping;
				}
				return array;
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		tableViewer.setInput(TypeNameMapping.getMapping());

	}

	private void addButtons(Composite parent) {
		final Composite par = parent;

		Composite composite = new Composite(par, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		Button addButton = new Button(composite, SWT.BUTTON1);
		addButton.setText(Messages.getString("Mapping.Add"));
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				AddMappingDialog dialog = new AddMappingDialog(par.getShell());
				dialog.open();
				if (dialog.getType() != null && dialog.getName() != null
						&& !"".equals(dialog.getType())
						&& !"".equals(dialog.getName()))
					TypeNameMapping.addMapping(dialog.getType(), dialog
							.getName());
				tableViewer.refresh();
			}
		});

		Button updateButton = new Button(composite, SWT.BUTTON1);
		updateButton.setText(Messages.getString("Mapping.Update"));
		updateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				if (!selection.isEmpty()) {
					Mapping mapping = (Mapping) selection.getFirstElement();
					UpdateMappingDialog dialog = new UpdateMappingDialog(par
							.getShell());
					dialog.setType(mapping.getType());
					dialog.setName(mapping.getName());
					dialog.open();
					if (dialog.getType() != null && dialog.getName() != null
							&& !"".equals(dialog.getType())
							&& !"".equals(dialog.getName()))
						TypeNameMapping.updateMapping(dialog.getType(), dialog
								.getName());
					tableViewer.refresh();
				}
			}
		});

		Button removeButton = new Button(composite, SWT.BUTTON1);
		final Composite comp = composite;
		removeButton.setText(Messages.getString("Mapping.Remove"));
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				if (!selection.isEmpty()) {
					Mapping mapping = (Mapping) selection.getFirstElement();
					if(MessageDialog.openQuestion(comp.getShell(),Messages.getString("Mapping.Remove.title"),Messages.getString("Mapping.Remove.message"))){
						TypeNameMapping.removeMapping(mapping.getType());
						tableViewer.refresh();
					}
				}
			}
		});
	}

	private class MappingTableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object element, int index) {
			Mapping mapping = (Mapping) element;
			switch (index) {
			case 0:
				return mapping.getType();
			case 1:
				return mapping.getName();
			default:
				return "unknown " + index;
			}
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("Mapping.title"));
	}

	private class Mapping {

		private String type;

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
