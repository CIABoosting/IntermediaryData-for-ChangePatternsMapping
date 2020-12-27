package org.jbpm.ui.wizard;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jbpm.ui.resource.Messages;
import org.jbpm.ui.util.TypeNameMapping;

public class SetInitializerWizardPage extends WizardPage {

	private List types;

	private String type;
	
	private boolean addReplacement = false;

	private Long numberOfParams;
	
	SetParamsWizardPage paramPage;

	protected SetInitializerWizardPage(List types) {
		super("Select type");
		this.types = types;
		setPageComplete(false);
		setTitle(Messages.getString("Swimlane.initializer.title"));
		setDescription(Messages.getString("Swimlane.initializer.description"));
	}

	public void createControl(Composite parent) {
		Composite wholePageComposite = new Composite(parent, SWT.NONE);
		setControl(wholePageComposite);
		wholePageComposite.setLayout(new GridLayout(1, false));
		wholePageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		final ListViewer typesList = new ListViewer(wholePageComposite,
				SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		GridData listData = new GridData(GridData.FILL_BOTH);
		listData.minimumHeight = 200;
		listData.minimumWidth = 100;
		typesList.getControl().setLayoutData(listData);
		typesList.setContentProvider(new ArrayContentProvider());
		typesList.setInput(types);
		typesList.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return TypeNameMapping.getTypeName((String) element);
			}
		});
		typesList.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) ((ListViewer) event
						.getSource()).getSelection();
				type = (String) selection.getFirstElement();
				if(numberOfParams != null){
					setPageComplete(true);
				}
			}

		});

		Composite paramComposite = new Composite(wholePageComposite, SWT.NONE);
		paramComposite.setLayout(new GridLayout(2, false));
		paramComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		Label numberOfParamsLabel = new Label(paramComposite, SWT.NONE);
		numberOfParamsLabel.setText(Messages.getString("Swimlane.initializer.numberofparams"));
		final Text numberOfParamsText = new Text(paramComposite, SWT.BORDER);
		numberOfParamsText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});
		numberOfParamsText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if ((numberOfParamsText.getText() != null)
						&& (!"".equals(numberOfParamsText.getText()))) {
					numberOfParams = new Long(numberOfParamsText.getText());
					if(type != null)
						setPageComplete(true);
				}
				else{
					numberOfParams = null;
					setPageComplete(false);
				}
			}

		});
		Label replacementLabel = new Label(paramComposite, SWT.NONE);
		replacementLabel.setText(Messages.getString("Swimlane.initializer.replacement"));
		Button replacementButton = new Button(paramComposite, SWT.CHECK);
		replacementButton.addSelectionListener(new SelectionListener(){

			public void widgetSelected(SelectionEvent e) {
				addReplacement = !addReplacement;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
	}

	@Override
	public IWizardPage getNextPage() {
		paramPage = new SetParamsWizardPage(addReplacement);
		paramPage.setNumberOfParams(numberOfParams);
		paramPage.setWizard(getWizard());
		return paramPage;
	}

	public String getType() {
		return type;
	}

	public Long getNumberOfParams() {
		return numberOfParams;
	}

	public SetParamsWizardPage getParamPage() {
		return paramPage;
	}

	public boolean isAddReplacement() {
		return addReplacement;
	}
	
}
