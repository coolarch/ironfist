/**
 * 
 */
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

import static cool.arch.ironfist.pmd.util.ClassMetadataAdapter.of;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cool.arch.ironfist.pmd.util.ClassMetadataAdapter;

/**
 * 
 */
public class ClassMetadataAdapterTest {

	private ClassMetadataAdapter<ClassMetadataAdapterTest> specimen;

	/**
	 * 
	 */
	@Before
	public void setup() {
		specimen = ClassMetadataAdapter.of(ClassMetadataAdapterTest.class);
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#getShortName()}.
	 */
	@Test
	public void testGetShortName() {
		assertEquals("ClassMetadataAdapterTest", specimen.getShortName());
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#getLongName()}.
	 */
	@Test
	public void testGetLongName() {
		assertEquals("cool.arch.ironfist.pmd.util.ClassMetadataAdapterTest", specimen.getLongName());
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#getPackageName()}.
	 */
	@Test
	public void testGetPackageName() {
		assertEquals("cool.arch.ironfist.pmd.util", specimen.getPackageName());
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#isFinal()}.
	 */
	@Test
	public void testIsFinal() {
		assertTrue(of(FinalClass.class).isFinal());
		assertFalse(of(UnmodifiedClass.class).isFinal());
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#isAbstract()}.
	 */
	@Test
	public void testIsAbstract() {
		assertTrue(of(AbstractClass.class).isAbstract());
		assertFalse(of(UnmodifiedClass.class).isAbstract());
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#getAdaptedClass()}.
	 */
	@Test
	public void testGetAdaptedClass() {
		assertSame(ClassMetadataAdapterTest.class, specimen.getAdaptedClass());
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#asStaticImportName(java.lang.String)}.
	 */
	@Test
	public void testAsStaticImportName() {
		assertEquals("cool.arch.ironfist.pmd.util.ClassMetadataAdapterTest.foo", specimen.asStaticImportName("foo"));
	}

	/**
	 * Test method for {@link cool.arch.ironfist.pmd.util.ClassMetadataAdapter#asImportedInvocationName(java.lang.String)}.
	 */
	@Test
	public void testAsImportedInvocationName() {
		assertEquals("ClassMetadataAdapterTest.foo", specimen.asImportedInvocationName("foo"));
	}

	static class UnmodifiedClass {
		// Intentionally empty
	}

	static final class FinalClass {
		// Intentionally empty
	}

	static abstract class AbstractClass {
		// Intentionally empty
	}
}
