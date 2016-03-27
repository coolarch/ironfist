package cool.arch.ironfist.pmd.rule.naming;

/*
 * @formatter:off
 * cool.arch.ironfist:ironfist-pmd
 * %%
 * Copyright (C) 2015 - 2016 CoolArch
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

public class DictionaryVariableNameRuleTest {

	private DictionaryVariableNameRule specimen;

	@Mock
	private RuleContext contextMock;

	@Mock
	private LanguageVersion languageVersionMock;

	@Mock
	private LanguageVersionHandler languageVersionHandlerMock;

	@Mock
	private RuleViolationFactory ruleViolationFactoryMock;

	@Before
	public void setUp() {
		initMocks(this);
		when(contextMock.getLanguageVersion()).thenReturn(languageVersionMock);
		when(languageVersionMock.getLanguageVersionHandler()).thenReturn(languageVersionHandlerMock);
		when(languageVersionHandlerMock.getRuleViolationFactory()).thenReturn(ruleViolationFactoryMock);
		specimen = new DictionaryVariableNameRule();
	}

	@Test
	public void testVisitInvalidWord() {
		final ASTVariableDeclaratorId node = new ASTVariableDeclaratorId(0);
		node.setImage("abcdefghijklmnopqrstuvwxyz");

		specimen.visit(node, contextMock);

		verify(ruleViolationFactoryMock, times(1)).addViolation(eq(contextMock), any(), any(), any(), any());
	}

	@Test
	public void testVisitValidWord() {
		final ASTVariableDeclaratorId node = new ASTVariableDeclaratorId(0);
		node.setImage("valid");

		specimen.visit(node, contextMock);

		verify(ruleViolationFactoryMock, times(0)).addViolation(eq(contextMock), any(), any(), any(), any());
	}
}
