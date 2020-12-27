package org.jbpm.ui.bsh;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.ui.bsh.BSHTypeSupport.StringType;
import org.jbpm.ui.model.Variable;

public class Operation {
	private static final List<Operation> OPERATIONS_LIST = new ArrayList<Operation>();

	static {
		registerOperation(new Operation("равн?", "==") {

			@Override
			public String generateCode(Variable variable, Object lexem2) {
				BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(variable.getFormat());
				if (typeSupport == StringType.INSTANCE) {
					StringBuffer buffer = new StringBuffer();
					buffer.append(typeSupport.wrap(variable));
					buffer.append(".equals(");
					buffer.append(typeSupport.wrap(lexem2));
					buffer.append(")");
					return buffer.toString();
				}
				return super.generateCode(variable, lexem2);
			}
			
		});
		registerOperation(new Operation("не равн?", "!=") {
			
			@Override
			public String generateCode(Variable variable, Object lexem2) {
				BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(variable.getFormat());
				if (typeSupport == StringType.INSTANCE) {
					StringBuffer buffer = new StringBuffer("!");
					buffer.append(typeSupport.wrap(variable));
					buffer.append(".equals(");
					buffer.append(typeSupport.wrap(lexem2));
					buffer.append(")");
					return buffer.toString();
				}
				return super.generateCode(variable, lexem2);
			}

		});
	}

	static void registerOperation(Operation operation) {
		OPERATIONS_LIST.add(operation);
	}

	private String visibleName;

	private String operator;

	public Operation(String visibleName, String operator) {
		this.visibleName = visibleName;
		this.operator = operator;
	}

	public String getVisibleName() {
		return visibleName;
	}

	public String getOperator() {
		return operator;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		return operator.hashCode() + 37*visibleName.hashCode();
	}

	public String generateCode(Variable variable, Object lexem2) {
		BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(variable.getFormat());

		StringBuffer buffer = new StringBuffer();
		buffer.append(typeSupport.wrap(variable));
		buffer.append(" ");
		buffer.append(getOperator());
		buffer.append(" ");
		buffer.append(typeSupport.wrap(lexem2));

		return buffer.toString();
	}

	public static List<Operation> getAll(BSHTypeSupport typeSupport) {
		List<Operation> allWithExt = new ArrayList<Operation>();
		allWithExt.addAll(OPERATIONS_LIST);
		List<Operation> extOperations = typeSupport.getTypedOperations();
		if (extOperations != null) {
			allWithExt.addAll(extOperations);
		}
		return allWithExt;
	}

	public static Operation getByName(String name, BSHTypeSupport typeSupport) {
		for (Operation operation : getAll(typeSupport)) {
			if (operation.getVisibleName().equals(name)) {
				return operation;
			}
		}
		return null;
	}

	public static Operation getByOperator(String operator, BSHTypeSupport typeSupport) {
		for (Operation operation : getAll(typeSupport)) {
			if (operation.getOperator().equals(operator)) {
				return operation;
			}
		}
		return null;
	}
}
