package org.jbpm.ui.util;

import java.util.HashMap;
import java.util.Map;

public class TypeNameMapping {

	private static Map<String, String> mapping = new HashMap<String, String>();

	public static String getTypeName(String key) {
		if (mapping.size() == 0) {
			MappingContentProvider.INSTANCE.addMappingInfo();
		}
		if (mapping.containsKey(key))
			return (String) mapping.get(key);
		else
			return key;
	}

	public static Map getMapping() {
		if (mapping.size() == 0){	
			MappingContentProvider.INSTANCE.addMappingInfo();
		}
		return mapping;
	}

	public static void setMapping(Map<String, String> newMapping) {
		mapping = newMapping;
	}

	public static void addMapping(String key, String value) {
		mapping.put(key, value);
	}

	public static void removeMapping(String key) {
		mapping.remove(key);
	}

	public static void updateMapping(String key, String value) {
		mapping.put(key, value);
	}

}
