package org.jbpm.ui.bsh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.ui.DesignerLogger;
import org.jbpm.ui.model.Variable;

public abstract class BSHTypeSupport {
	private static final String DEFAULT_FORMAT_NAME = "org.jbpm.web.formgen.format.DefaultFormat";

	private static final Map<String, BSHTypeSupport> BSH_TYPES_MAP = new HashMap<String, BSHTypeSupport>();
	static {
		BSH_TYPES_MAP.put(DEFAULT_FORMAT_NAME, StringType.INSTANCE);
		BSH_TYPES_MAP.put("ru.runa.wf.web.forms.format.StringFormat", StringType.INSTANCE);
		BSH_TYPES_MAP.put("ru.runa.wf.web.forms.format.BooleanFormat", BooleanType.INSTANCE);
		BSH_TYPES_MAP.put("org.jbpm.web.formgen.format.DoubleFormat", NumberType.INSTANCE);
		BSH_TYPES_MAP.put("ru.runa.wf.web.forms.format.LongFormat", NumberType.INSTANCE);
		BSH_TYPES_MAP.put("ru.runa.wf.web.forms.format.DateFormat", DateType.INSTANCE);
		BSH_TYPES_MAP.put("ru.runa.wf.web.forms.format.TimeFormat", DateType.INSTANCE);
		BSH_TYPES_MAP.put("ru.runa.wf.web.forms.format.DateTimeFormat", DateType.INSTANCE);
	}
	
	public static BSHTypeSupport getByFormat(String format) {
		if (format == null) {
			format = DEFAULT_FORMAT_NAME;
		}
		BSHTypeSupport typeSupport = BSH_TYPES_MAP.get(format);
		if (typeSupport == null) {
			DesignerLogger.logError("Not found type support for format: " + format, new IllegalArgumentException("Not found type support for format: " + format));
			// typeSupport = BSH_TYPES_MAP.get(DEFAULT_FORMAT_NAME);
		}
		return typeSupport;
	}

	abstract String wrap(Object value);

	public String unwrapValue(String value) {
		return value;
	}

	public Variable unwrapVariable(String variableString) {
		return null;
	}

	abstract List<Operation> getTypedOperations();

	static class StringType extends BSHTypeSupport {
		protected static final BSHTypeSupport INSTANCE = new StringType();

		@Override
		String wrap(Object value) {
			if (value instanceof Variable) {
				return ((Variable) value).getName();
			} else if (value instanceof String) {
				return "\"" + value + "\"";
			} else {
				throw new IllegalArgumentException("value class is " + value.getClass().getName());
			}
		}

		public String unwrapValue(String value) {
			return value.substring(1, value.length()-1);
		}

		@Override
		List<Operation> getTypedOperations() {
			return null;
		}
	}

	static class BooleanType extends BSHTypeSupport {
		protected static final BSHTypeSupport INSTANCE = new BooleanType();

		@Override
		String wrap(Object value) {
			if (value instanceof Variable) {
				return ((Variable) value).getName() + ".booleanValue()";
			} else if (value instanceof String) {
				return (String) value;
			} else {
				throw new IllegalArgumentException("value class is " + value.getClass().getName());
			}
		}

		@Override
		List<Operation> getTypedOperations() {
			return null;
		}
	}

	static class NumberType extends BSHTypeSupport {
		protected static final BSHTypeSupport INSTANCE = new NumberType();
		
		@Override
		String wrap(Object value) {
			if (value instanceof Variable) {
				return ((Variable) value).getName() + ".longValue()";
			} else if (value instanceof String) {
				return (String) value;
			} else {
				throw new IllegalArgumentException("value class is " + value.getClass().getName());
			}
		}

		@Override
		List<Operation> getTypedOperations() {
			List<Operation> extOperations = new ArrayList<Operation>();
			extOperations.add(new Operation("больше", ">"));
			extOperations.add(new Operation("меньше", "<"));
			return extOperations;
		}
	}

	static class DateType extends BSHTypeSupport {
		protected static final BSHTypeSupport INSTANCE = new DateType();

		@Override
		String wrap(Object value) {
			if (value instanceof Variable) {
				return ((Variable) value).getName() + ".getTime()";
			} else if (value instanceof String) {
				return (String) value;
			} else {
				throw new IllegalArgumentException("value class is " + value.getClass().getName());
			}
		}

		@Override
		List<Operation> getTypedOperations() {
			List<Operation> extOperations = new ArrayList<Operation>();
			extOperations.add(new Operation("ране?", ">"));
			extOperations.add(new Operation("позж?", "<"));
			return extOperations;
		}
	}
}
