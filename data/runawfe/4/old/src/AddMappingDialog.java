package org.jbpm.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jbpm.ui.resource.Messages;

public class AddMappingDialog extends Dialog {

	private String type;
	
	private String name;
	
	private Text nameField;

	private Text typeField;
	
	protected AddMappingDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1, false);
		area.setLayout(layout);
		final Label labelTitle = new Label(area, SWT.NO_BACKGROUND);
		final GridData labelData = new GridData();
		labelTitle.setLayoutData(labelData);
		labelTitle.setText(Messages.getString("Mapping.add.message"));

		final Composite composite = new Composite(area, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		final GridData nameData = new GridData();
		composite.setLayoutData(nameData);
		
		final Label labelType = new Label(composite, SWT.NONE);
		labelType.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		labelType.setText(Messages.getString("Variable.property.format") + ":");
		typeField = new Text(composite, SWT.BORDER);
		GridData typeTextData = new GridData(GridData.FILL_HORIZONTAL);
		typeTextData.minimumWidth = 200;
		typeField.setLayoutData(typeTextData);
		typeField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				type = typeField.getText();
			}
		});
		final Label labelName = new Label(composite, SWT.NONE);
		labelName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		labelName.setText(Messages.getString("Variable.property.name") + ":");
		nameField = new Text(composite, SWT.BORDER);
		GridData nameTextData = new GridData(GridData.FILL_HORIZONTAL);
		nameTextData.minimumWidth = 200;
		nameField.setLayoutData(nameTextData);
		nameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				name = nameField.getText();
			}
		});
		
		return area;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("Mapping.add.title"));
	}
	
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
