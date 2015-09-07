package cool.arch.ironfist.pmd.rule.legacy;

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

import org.junit.Test;

public class DisallowedGauvaPreconditionsCheckNotNullJavaRuleTest extends DisallowedGauvaPreconditionsCheckNotNullJavaRuleSpec {

	@Test
	public void testVisitStaticImport() {
		givenAnImport("com.google.common.base.Preconditions.checkNotNull");
		givenAStaticImport();
		givenAStatementName("checkNotNull");
		whenVisited();
		thenViolationsWereAdded();
	}

	@Test
	public void testVisitImport() {
		givenAnImport("com.google.common.base.Preconditions");
		givenAStatementName("Preconditions.checkNotNull");
		whenVisited();
		thenViolationsWereAdded();
	}

	@Test
	public void testVisitNoImport() {
		givenAnImport("java.util.List");
		givenAStatementName("com.google.common.base.Preconditions.checkNotNull");
		whenVisited();
		thenViolationsWereAdded();
	}

	@Test
	public void testVisitControlCase() {
		givenAnImport("java.util.List");
		givenAStatementName("String.format");
		whenVisited();
		thenNoViolationsWereAdded();
	}
}
