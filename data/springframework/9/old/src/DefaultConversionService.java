/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http:
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.binding.convert.support;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.Converter;
import org.springframework.binding.format.FormatterLocator;
import org.springframework.binding.format.support.ThreadLocalFormatterLocator;
import org.springframework.binding.support.Mapping;
import org.springframework.binding.support.TextToMapping;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Default, local implementation of a conversion service.
 * <p>
 * Acts as bean factory post processor, registering property editor adapters for
 * each supported conversion with a <code>java.lang.String sourceClass</code>.
 * This makes for very convenient use with the Spring container.
 * @author Keith Donald
 */
public class DefaultConversionService implements ConversionService {

	private Map aliasMap = new HashMap();
	
	private ConversionService parent;
	
	private Map sourceClassConverters = new HashMap();

	private FormatterLocator formatterLocator = new ThreadLocalFormatterLocator();

	protected DefaultConversionService() {

	}

	public DefaultConversionService(ConversionService parent) {
		setParent(parent);
	}

	public void setParent(ConversionService parent) {
		this.parent = parent;
	}
	
	public void setFormatterLocator(FormatterLocator formatterLocator) {
		this.formatterLocator = formatterLocator;
	}

	public void setConverters(Converter[] converters) {
		this.sourceClassConverters = new HashMap(converters.length);
		addConverters(converters);
	}

	protected void addDefaultConverters() {
		addConverter(new TextToClass());
		addConverter(new TextToNumber(getFormatterLocator()));
		addConverter(new TextToBoolean());
		addConverter(new TextToMapping(this));
		addDefaultAlias(Short.class);
		addDefaultAlias(Integer.class);
		addDefaultAlias(Long.class);
		addDefaultAlias(Float.class);
		addDefaultAlias(Double.class);
		addDefaultAlias(BigInteger.class);
		addDefaultAlias(Boolean.class);
		addDefaultAlias(Mapping.class);
		addDefaultAlias(Class.class);
	}

	public void addAlias(String alias, Class targetType) {
		aliasMap.put(alias, targetType);
	}
	
	public void addDefaultAlias(Class targetType) {
		addAlias(StringUtils.uncapitalize(ClassUtils.getShortName(targetType)), targetType);
	}

	public Class withAlias(String alias) throws IllegalArgumentException {
		Class targetType = (Class)aliasMap.get(alias);
		if (targetType == null) {
			ConversionExecutor executor = getConversionExecutor(String.class, Class.class);
			targetType = (Class)executor.execute(alias);
		}
		return targetType;
	}
	

	public void addConverters(Converter[] converters) {
		for (int i = 0; i < converters.length; i++) {
			addConverter(converters[i]);
		}
	}

	public void addConverter(Converter converter) {
		Class[] sourceClasses = converter.getSourceClasses();
		Class[] targetClasses = converter.getTargetClasses();
		for (int i = 0; i < sourceClasses.length; i++) {
			Class sourceClass = sourceClasses[i];
			Map sourceMap = (Map)this.sourceClassConverters.get(sourceClass);
			if (sourceMap == null) {
				sourceMap = new HashMap();
				this.sourceClassConverters.put(sourceClass, sourceMap);
			}
			for (int j = 0; j < targetClasses.length; j++) {
				Class targetClass = targetClasses[j];
				sourceMap.put(targetClass, converter);
			}
		}
	}

	protected FormatterLocator getFormatterLocator() {
		return formatterLocator;
	}

	public ConversionExecutor getConversionExecutor(Class sourceClass, Class targetClass) {
		if (this.sourceClassConverters == null || this.sourceClassConverters.isEmpty()) {
			throw new IllegalStateException("No converters have been added to this service's registry");
		}
		if (sourceClass.equals(targetClass)) {
			throw new IllegalArgumentException("Source class '" + sourceClass
					+ "' already equals target class; no conversion to perform");
		}
		Map sourceTargetConverters = (Map)findConvertersForSource(sourceClass);
		Converter converter = (Converter)sourceTargetConverters.get(targetClass);
		if (converter != null) {
			return new ConversionExecutor(converter, targetClass);
		}
		else {
			if (this.parent != null) {
				return this.parent.getConversionExecutor(sourceClass, targetClass);
			} else {
				throw new IllegalArgumentException("No converter registered to convert from sourceClass '" + sourceClass
						+ "' to target class '" + targetClass + "'");
			}
		}
	}

	protected Map findConvertersForSource(Class sourceClass) {
		LinkedList classQueue = new LinkedList();
		classQueue.addFirst(sourceClass);
		while (!classQueue.isEmpty()) {
			sourceClass = (Class)classQueue.removeLast();
			Map sourceTargetConverters = (Map)sourceClassConverters.get(sourceClass);
			if (sourceTargetConverters != null && !sourceTargetConverters.isEmpty()) {
				return sourceTargetConverters;
			}
			if (!sourceClass.isInterface() && (sourceClass.getSuperclass() != null)) {
				classQueue.addFirst(sourceClass.getSuperclass());
			}
			Class[] interfaces = sourceClass.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				classQueue.addFirst(interfaces[i]);
			}
		}
		return Collections.EMPTY_MAP;
	}

	public ConversionService getParent() {
		return parent;
	}
	
	protected Map getSourceClassConverters() {
		return sourceClassConverters;
	}
	
	protected Map getAliasMap() {
		return aliasMap;
	}
}
