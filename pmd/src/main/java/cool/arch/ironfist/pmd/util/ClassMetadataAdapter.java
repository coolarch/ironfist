package cool.arch.ironfist.pmd.util;

/*
 * @formatter:off
 * cool.arch.ironfist:ironfist-pmd
 * %%
 * Copyright (C) 2015 CoolArch
 * %%
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * @formatter:on
 */

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Modifier;

/**
 * Adapter class to simplify extraction of information from a class.
 * @param <T> Type of the class
 */
public final class ClassMetadataAdapter<T> {

	private final Class<T> adaptedClass;

	private final String shortName;

	private final String longName;

	private final String packageName;

	private ClassMetadataAdapter(final Class<T> adaptedClass) {
		this.adaptedClass = requireNonNull(adaptedClass, "adaptedClass shall not be null");
		packageName = adaptedClass.getPackage()
			.getName();
		longName = adaptedClass.getName();
		shortName = longName.substring(packageName.length() + 1);
	}

	/**
	 * 
	 * @param adaptedClass
	 * @return
	 */
	public static final <T> ClassMetadataAdapter<T> of(final Class<T> adaptedClass) {
		return new ClassMetadataAdapter<>(adaptedClass);
	}

	/**
	 * 
	 * @return
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * 
	 * @return
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * 
	 * @return
	 */
	public Class<T> getAdaptedClass() {
		return adaptedClass;
	}

	/**
	 * 
	 * @return
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFinal() {
		return Modifier.isFinal(adaptedClass.getModifiers());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAbstract() {
		return Modifier.isAbstract(adaptedClass.getModifiers());
	}

	/**
	 * 
	 * @param methodName
	 * @return
	 */
	public String asStaticImportName(final String methodName) {
		requireNonNull(methodName, "methodName shall not be null");

		return getLongName() + "." + methodName;
	}

	/**
	 * 
	 * @param methodName
	 * @return
	 */
	public String asImportedInvocationName(final String methodName) {
		requireNonNull(methodName, "methodName shall not be null");

		return getShortName() + "." + methodName;
	}
}
