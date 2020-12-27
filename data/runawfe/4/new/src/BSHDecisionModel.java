package org.jbpm.ui.bsh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbpm.ui.model.Variable;

public class BSHDecisionModel {
	private Map<String, IfExpr> ifs = new HashMap<String, IfExpr>();
	private String defaultReturnPath;

	private static List<Variable> variables;

	private static Pattern IF_PATTERN = Pattern.compile("if \\((.*)\\)");

	private static Pattern RETURN_PATTERN = Pattern.compile("return \"(.*)\";");

	public static BSHDecisionModel fromCode(String code, List<Variable> vars) throws Exception {
		variables = vars;

		BSHDecisionModel decisionModel = new BSHDecisionModel();

		Matcher returnMatcher = RETURN_PATTERN.matcher(code);

		Matcher matcher = IF_PATTERN.matcher(code);
		int startReturnSearch = 0;
		while (matcher.find()) {
			String returnValue;
			String ifContent = normalizeString(matcher.group(1));
			String[] strings = ifContent.split(" ");
			// tmp
			String lexem1Text = "";
			String operator;
			String lexem2Text = "";
			if (strings.length == 1) {
				// i.e. var1.equals(var2)
				String eqString = strings[0];
				int start;
				if (eqString.charAt(0) != '!') {
					start = 0;
					operator = "==";
				} else {
					start = 1;
					operator = "!=";
				}
				lexem1Text = eqString.substring(start, eqString.indexOf("."));
				lexem2Text = eqString.substring(eqString.indexOf("(") + 1, eqString.length()-1);
			} else {
				lexem1Text = strings[0];
				operator = strings[1];
				if (strings.length == 3) {
					lexem2Text = strings[2];
				} else {
					for (int i = 2; i < strings.length; i++) {
						lexem2Text += " " + strings[i];
					}
				}
			}
			startReturnSearch = matcher.end(1);
			if (returnMatcher.find(startReturnSearch)) {
				returnValue = returnMatcher.group(1);
				startReturnSearch = returnMatcher.end(1);
			} else {
				throw new RuntimeException("unparsed");
			}

			if (lexem1Text.indexOf(".") > 0) {
				// Java names doesn't allowed use of point in variable name
				lexem1Text = lexem1Text.substring(0, lexem1Text.indexOf("."));
			}
			Variable var1 = getVariableByName(lexem1Text);
			if (var1 == null) {
				// variable deleted
				continue;
			}
			BSHTypeSupport typeSupport = BSHTypeSupport.getByFormat(var1.getFormat());

			Operation operation = Operation.getByOperator(operator, typeSupport);
			if (operation == null) {
				throw new NullPointerException("operation not found for operator: " + operator);
			}

			Object lexem2;
			if (lexem2Text.indexOf(".") > 0) {
				// Java names doesn't allowed use of point in variable name
				lexem2Text = lexem2Text.substring(0, lexem2Text.indexOf("."));
			}
			Variable var2 = getVariableByName(lexem2Text);
			if (var2 != null) {
				lexem2 = var2;
			} else {
				lexem2 = typeSupport.unwrapValue(lexem2Text);
			}
			IfExpr ifExpr = new IfExpr(var1, lexem2, operation, returnValue);
			decisionModel.addIfExpr(ifExpr);
		}

		if (returnMatcher.find(startReturnSearch)) {
			String defaultPath = returnMatcher.group(1);
			decisionModel.setDefaultReturnPath(defaultPath);
		} else {
			throw new RuntimeException("unparsed");
		}

		return decisionModel;
	}

	public void setDefaultReturnPath(String defaultReturnPath) {
		this.defaultReturnPath = defaultReturnPath;
	}

	public String getDefaultReturnPath() {
		return defaultReturnPath;
	}

	private static Variable getVariableByName(String variableName) {
		for (Variable variable : variables) {
			if (variable.getName().equals(variableName)) {
				return variable;
			}
		}
		return null;
	}

	private static String normalizeString(String str) {
		while (str.charAt(0) == ' ') {
			str = str.substring(1);
		}
		while (str.charAt(str.length() - 1) == ' ') {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public void addIfExpr(IfExpr ifExpr) {
		ifs.put(ifExpr.returnName, ifExpr);
	}

	public IfExpr getIfExpr(String returnName) {
		return ifs.get(returnName);
	}

	public String generateCode() {
		StringBuffer buffer = new StringBuffer();
		for (IfExpr ifExpr : ifs.values()) {
			buffer.append(ifExpr.generateCode());
		}
		buffer.append("\nreturn \"" + defaultReturnPath + "\";\n");
		return buffer.toString();
	}

	public static class IfExpr {
		private Variable variable;

		private Object lexem2;

		private Operation operation;

		private String returnName;

		public IfExpr(Variable variable, Object lexem2, Operation operation, String returnName) {
			this.variable = variable;
			this.lexem2 = lexem2;
			this.operation = operation;
			this.returnName = returnName;
		}

		public String generateCode() {
			return "if ( " + operation.generateCode(variable, lexem2) + " ) {\n\treturn \"" + returnName + "\";\n}\n";
		}

		public Variable getVariable1() {
			return variable;
		}

		public Object getLexem2() {
			return lexem2;
		}

		public String getLexem2TextValue() {
			if (lexem2 instanceof Variable) {
				return ((Variable) lexem2).getName();
			} else if (lexem2 instanceof String) {
				return (String) lexem2;
			} else {
				throw new IllegalArgumentException("lexem2 class is " + lexem2.getClass().getName());
			}
		}

		public Operation getOperation() {
			return operation;
		}

		public String getReturnName() {
			return returnName;
		}

	}
}
