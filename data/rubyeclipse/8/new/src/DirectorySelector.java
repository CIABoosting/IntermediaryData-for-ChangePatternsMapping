package org.rubypeople.rdt.internal.ui.utils;

import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

public class DirectorySelector extends ResourceSelector {

	public DirectorySelector(Composite parent) {
		super(parent);
	}

	protected void handleBrowseSelected() {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setMessage(browseDialogMessage);
		String currentWorkingDir = textField.getText();
		if (!currentWorkingDir.trim().equals("")) {
			File path = new File(currentWorkingDir);
			if (path.exists()) {
				dialog.setFilterPath(currentWorkingDir);
			}			
		}
		
		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			textField.setText(selectedDirectory);
		}		
	}
}
