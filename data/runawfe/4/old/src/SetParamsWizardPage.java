package org.jbpm.ui.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jbpm.ui.resource.Messages;

public class SetParamsWizardPage extends WizardPage {

	private Long numberOfParams;

	private List params;
	
	private boolean addReplacement = false;
	
	private boolean empty= true;
	
	private String replacement;

	protected SetParamsWizardPage(boolean addReplacement) {
		super("Input params");
		this.addReplacement = addReplacement;
		params = new ArrayList();
		setPageComplete(false);
		setTitle(Messages.getString("Swimlane.initializer.title"));
		setDescription(Messages.getString("Swimlane.params.description"));
	}

	public void createControl(Composite parent) {
		Composite wholePageComposite = new Composite(parent, SWT.NONE);
		setControl(wholePageComposite);
		wholePageComposite.setLayout(new GridLayout(2, false));
		wholePageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		if ((numberOfParams != null) && (numberOfParams.longValue() != 0) ) {
			empty = false;
			((WizardPage)getWizard().getPage(getName())).setPageComplete(false);
			for (int i = 0; i < numberOfParams; i++) {
				params.add(i, "");
				final int index = i;
				Label label = new Label(wholePageComposite, SWT.NONE);
				label.setText(Messages.getString("Swimlane.params.parameter") + i + " : ");
				final Text text = new Text(wholePageComposite, SWT.BORDER);
				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				text.addModifyListener(new ModifyListener(){

					public void modifyText(ModifyEvent e) {
						params.set(index, text.getText());
						for (Iterator iter = params.iterator(); iter
								.hasNext();) {
							String element = (String) iter.next();
							if("".equals(element)){
								((WizardPage)getWizard().getPage(getName())).setPageComplete(false);
								setPageComplete(false);
								return;
							}
						}
						((WizardPage)getWizard().getPage(getName())).setPageComplete(true);
						setPageComplete(true);
					}
					
				});
			}
		} 
		
		if(addReplacement && ((SwimlaneWizard)getWizard()).addReplacementString){
			empty = false;
			Label label = new Label(wholePageComposite, SWT.NONE);
			label.setText(Messages.getString("Swimlane.params.replacement"));
			final Text text = new Text(wholePageComposite, SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			text.addModifyListener(new ModifyListener(){

				public void modifyText(ModifyEvent e) {
					replacement = text.getText();
				}
				
			});
		}
		
		if(empty) {
			Label label = new Label(wholePageComposite, SWT.NONE);
			label.setText(Messages.getString("Swimlane.params.finish"));
			if(numberOfParams != null){
				((WizardPage)getWizard().getPage(getName())).setPageComplete(true);
				setPageComplete(true);
			}
		}

	}

	public Long getNumberOfParams() {
		return numberOfParams;
	}

	public void setNumberOfParams(Long numberOfParams) {
		this.numberOfParams = numberOfParams;
	}

	public List getParams() {
		return params;
	}

	public String getReplacement() {
		return replacement;
	}
	
	

}
