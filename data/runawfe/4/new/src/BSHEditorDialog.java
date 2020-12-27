package org.jbpm.ui.dialog;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jbpm.ui.DesignerLogger;
import org.jbpm.ui.bsh.BSHDecisionModel;
import org.jbpm.ui.bsh.BSHTypeSupport;
import org.jbpm.ui.bsh.Operation;
import org.jbpm.ui.bsh.BSHDecisionModel.IfExpr;
import org.jbpm.ui.model.Variable;
import org.jbpm.ui.resource.Messages;

public class BSHEditorDialog extends Dialog implements SelectionListener {
	private TabFolder tabFolder;

	// text editor
	private Text text;

	private Composite constructor;

	private String initValue;

	private List<String> transitionNames;

	private List<Variable> variables;

	private Composite headerComposite; 
		
	private Label errorLabel;

	private Link createNewLink;

	private Combo[][] comboBoxes;

	private Combo defaultTransitionCombo;

	private String result;

	public BSHEditorDialog(Shell parent, String initValue) {
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.initValue = initValue;
	}

	protected Point getInitialSize() {
		return new Point(500, 400);
	}

	public void setTransitionNames(List<String> transitionNames) {
		this.transitionNames = transitionNames;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("BSHEditor.title"));

		tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayout(new GridLayout());
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.addSelectionListener(this);

		TabItem[] tabs = new TabItem[2];

		text = new Text(tabFolder, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		text.setText(this.initValue);

		tabs[0] = new TabItem(tabFolder, SWT.NULL);
		tabs[0].setText(Messages.getString("BSHEditor.title.constructor"));

		Composite composite = new Composite(tabFolder, SWT.BORDER);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		tabs[0].setControl(composite);

		headerComposite = new Composite(composite, SWT.BORDER);
		headerComposite.setLayout(new GridLayout(2, true));
		headerComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		errorLabel = new Label(headerComposite, SWT.NONE);
		errorLabel.setForeground(ColorConstants.red);
		errorLabel.setText("");

		createNewLink = new Link(headerComposite, SWT.NONE);
		createNewLink.setText("<a>" + Messages.getString("BSHEditor.createNew") + "</a>");
		createNewLink.setCursor(Cursors.HAND);
		createNewLink.setVisible(false);
		createNewLink.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				createNewLink.setVisible(false);
				constructor.setEnabled(true);
			}

		});

		constructor = new Composite(composite, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		constructor.setLayout(gridLayout);
		constructor.setLayoutData(new GridData(GridData.FILL_BOTH));

		tabs[1] = new TabItem(tabFolder, SWT.NULL);
		tabs[1].setText(Messages.getString("BSHEditor.title.bsh"));
		tabs[1].setControl(text);

		Composite bottomComposite = new Composite(composite, SWT.NONE);
		bottomComposite.setLayout(new GridLayout(2, true));
		bottomComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label defaultLabel = new Label(bottomComposite, SWT.NONE);
		defaultLabel.setForeground(ColorConstants.blue);
		defaultLabel.setText(Messages.getString("BSHEditor.defaultPath") + ":");

		defaultTransitionCombo = new Combo(bottomComposite, SWT.READ_ONLY);
		for (String trName : transitionNames) {
			defaultTransitionCombo.add(trName);
		}
		bottomComposite.pack();

		createConstructorView();

		try {
			BSHDecisionModel decisionModel = null;
			if (this.initValue.length() > 0) {
				decisionModel = BSHDecisionModel.fromCode(this.initValue, variables);
				decisionModel.generateCode();
			}
			initConstructorView(decisionModel);
		} catch (Throwable e) {
			e.printStackTrace();
			DesignerLogger.logError(e);
			setErrorLabelText(Messages.getString("BSHEditor.error.parsing"));
			constructor.setEnabled(false);
			createNewLink.setVisible(true);
		}

		return tabFolder;
	}
	
	private void setErrorLabelText(String text) {
		errorLabel.setText(text);
		headerComposite.pack();
	}

	private void initConstructorView(BSHDecisionModel decisionModel) {
		if (decisionModel != null) {
			// fill areas
			for (int i = 0; i < transitionNames.size(); i++) {
				IfExpr ifExpr = decisionModel.getIfExpr(transitionNames.get(i));

				if (ifExpr != null) {
					Variable variable = ifExpr.getVariable1();
					int index = variables.indexOf(variable);
					if (index == -1) {
						// required variable was deleted in process definition
						continue;
					}
					comboBoxes[i][0].select(index);
					refreshComboItems(comboBoxes[i][0]);
	
					BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(variable.getFormat());
	
					index = Operation.getAll(typeSupport).indexOf(ifExpr.getOperation());
					if (index == -1) {
						// required operation was deleted !!!
						continue;
					}
					comboBoxes[i][1].select(index);
					refreshComboItems(comboBoxes[i][1]);
	
					comboBoxes[i][2].setText(ifExpr.getLexem2TextValue());
				} else {
					setErrorLabelText(Messages.getString("BSHEditor.error.parsing"));
				}
			}

			if (transitionNames.contains(decisionModel.getDefaultReturnPath())) {
				defaultTransitionCombo.setText(decisionModel.getDefaultReturnPath());
			}
		}

		if ((defaultTransitionCombo.getSelectionIndex() < 0) && (defaultTransitionCombo.getItemCount() > 0)) {
			defaultTransitionCombo.select(0);
		}

	}

	static final String DATA_INDEX_KEY = "indexes";
	static final String DATA_VARIABLE_KEY = "variable";
	
	private void createConstructorView() {
		comboBoxes = new Combo[transitionNames.size()][3];
		for (int i = 0; i < transitionNames.size(); i++) {
			Label label = new Label(constructor, SWT.NULL);
			label.setText(transitionNames.get(i));

			comboBoxes[i][0] = new Combo(constructor, SWT.READ_ONLY);
			for (Variable variable : variables) {
				comboBoxes[i][0].add(variable.getName());
			}
			comboBoxes[i][0].setData(DATA_INDEX_KEY, new int[] { i, 0 });
			comboBoxes[i][0].addSelectionListener(this);

			comboBoxes[i][1] = new Combo(constructor, SWT.READ_ONLY);
			comboBoxes[i][1].setData(DATA_INDEX_KEY, new int[] { i, 1 });
			comboBoxes[i][1].addSelectionListener(this);

			comboBoxes[i][2] = new Combo(constructor, SWT.DROP_DOWN);
			comboBoxes[i][2].setData(DATA_INDEX_KEY, new int[] { i, 2 });
		}
		for (int i = 0; i < comboBoxes.length; i++) {
			for (int j = 0; j < 3; j++) {
				comboBoxes[i][j].setSize(100, 20);
			}
		}
	}

	private Variable getVariableByName(String variableName) {
		for (Variable variable : variables) {
			if (variable.getName().equals(variableName)) {
				return variable;
			}
		}
		return null;
	}

	private void refreshComboItems(Combo combo) {
		try {
			int[] indexes = (int[]) combo.getData(DATA_INDEX_KEY);
			
			if (indexes[1] == 2) {
				return;
			}
			
			Combo targetCombo = comboBoxes[indexes[0]][indexes[1] + 1];
			targetCombo.setItems(new String[0]);
			if (indexes[1] == 0) {
				// there was changed value in first (variable) combo in 'i' row
				Variable variable = getVariableByName(combo.getText());
				combo.setData(DATA_VARIABLE_KEY, variable);
				BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(variable.getFormat());

				for (Operation operation : Operation.getAll(typeSupport)) {
					targetCombo.add(operation.getVisibleName());
				}
			} else if (indexes[1] == 1) {
				// there was changed value in second (operation) combo in 'i' row
				Variable variable1 = (Variable) comboBoxes[indexes[0]][0].getData(DATA_VARIABLE_KEY);
				BSHTypeSupport typeSupport1 = BSHTypeSupport.getByFormat(variable1.getFormat());
				for (Variable variable : variables) {
					BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(variable.getFormat());
					// formats are equals, variable not selected in the first combo
					if ((typeSupport1 == typeSupport) && (variable1 != variable)) {
						targetCombo.add(variable.getName());
					}
				}
			}
			//combo.pack();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void widgetSelected(SelectionEvent e) {
		try {
			if (e.widget == tabFolder) {
				// tab item changed
				if (tabFolder.getSelectionIndex() == 0) {
					// Constructor
				} else {
					// BSH code
					for (int i = 0; i < comboBoxes.length; i++) {
						for (int j = 0; j < 3; j++) {
							if (comboBoxes[i][j].getText().length() == 0) {
								setErrorLabelText(Messages.getString("BSHEditor.fillAll"));
								// we cannot construct while all data not filled
								return;
							}
						}
					}

					try {
						BSHDecisionModel decisionModel = new BSHDecisionModel();
						for (int i = 0; i < transitionNames.size(); i++) {
							Variable var1 = (Variable) comboBoxes[i][0].getData(DATA_VARIABLE_KEY);
							String operationName = comboBoxes[i][1].getItem(comboBoxes[i][1].getSelectionIndex());
							String lexem2Text = comboBoxes[i][2].getText();

							Object lexem2;
							Variable var2 = getVariableByName(lexem2Text);
							if (var2 != null) {
								lexem2 = var2;
							} else {
								lexem2 = lexem2Text;
							}

							BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(var1.getFormat());

							IfExpr ifExpr = new IfExpr(var1, lexem2, Operation.getByName(operationName, typeSupport), transitionNames.get(i));
							decisionModel.addIfExpr(ifExpr);
						}
						decisionModel.setDefaultReturnPath(defaultTransitionCombo.getText());
						text.setText(decisionModel.generateCode());
					} catch (RuntimeException e1) {
						e1.printStackTrace();
						DesignerLogger.logError(e1);
						errorLabel.setText(Messages.getString("BSHEditor.error.construct"));
					}
				}
			} else if (e.widget instanceof Combo) {
				refreshComboItems((Combo) e.widget);
			} 
		} catch (RuntimeException e1) {
			e1.printStackTrace();
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	protected void okPressed() {
		this.result = text.getText();
		super.okPressed();
	}

	public String getResult() {
		return result;
	}
}
