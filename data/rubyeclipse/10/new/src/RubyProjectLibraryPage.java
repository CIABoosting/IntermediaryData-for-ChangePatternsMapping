package org.rubypeople.rdt.internal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.rubypeople.rdt.internal.core.RubyPlugin;
import org.rubypeople.rdt.internal.core.RubyProject;

public class RubyProjectLibraryPage {
	protected RubyProject workingProject;

	protected RubyProjectLibraryPage(RubyProject theWorkingProject) {
		super();
		workingProject = theWorkingProject;
	}

	protected Control getControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		Table projectsTable = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		projectsTable.setHeaderVisible(false);
		projectsTable.setLinesVisible(false);
		projectsTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableColumn tableColumn = new TableColumn(projectsTable, SWT.NONE);
		tableColumn.setWidth(200);
		tableColumn.setText("Project");

		CheckboxTableViewer projectsTableViewer = new CheckboxTableViewer(projectsTable);
		projectsTableViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				projectCheckedUnchecked(event);
			}
		});

		projectsTableViewer.setContentProvider(getContentProvider());
		projectsTableViewer.setLabelProvider(getLabelProvider());

		projectsTableViewer.setInput(getWorkspaceProjects());
		projectsTableViewer.setCheckedElements(workingProject.getReferencedProjects().toArray());

		return composite;
	}

	protected void projectCheckedUnchecked(CheckStateChangedEvent event) {
		IProject checkEventProject = (IProject) event.getElement();
		if (event.getChecked())
			getWorkingProject().addLoadPathEntry(checkEventProject);
		else
			getWorkingProject().removeLoadPathEntry(checkEventProject);
	}

	protected RubyProject getWorkingProject() {
		return workingProject;
	}

	protected List getWorkspaceProjects() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return Arrays.asList(root.getProjects());
	}

	protected ITableLabelProvider getLabelProvider() {
		ITableLabelProvider labelProvider = new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				IWorkbench workbench= RdtUiPlugin.getDefault().getWorkbench();
				return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_PROJECT);
			}

			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof IProject)
					return ((IProject) element).getName();

				return "ERROR: Element not IProject";
			}

			public void addListener(ILabelProviderListener listener) {}

			public void dispose() {}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {}
		};

		return labelProvider;
	}

	protected IContentProvider getContentProvider() {
		IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
			protected List rubyProjects;

			public Object[] getElements(Object inputElement) {
				return rubyProjects.toArray();
			}

			public void dispose() {}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				rubyProjects = new ArrayList();

				if (!(newInput instanceof List))
					return;

				Iterator workspaceProjectsIterator = ((List) newInput).iterator();
				while (workspaceProjectsIterator.hasNext()) {
					Object anObject = workspaceProjectsIterator.next();
					if (anObject instanceof IProject) {
						IProject project = (IProject) anObject;
						if (project.getName() != workingProject.getProject().getName()) {
							try {
								if (project.hasNature(RubyPlugin.RUBY_NATURE_ID))
									rubyProjects.add(project);
							} catch (CoreException e) {}
						}
					}
				}
			}
		};

		return contentProvider;
	}
}