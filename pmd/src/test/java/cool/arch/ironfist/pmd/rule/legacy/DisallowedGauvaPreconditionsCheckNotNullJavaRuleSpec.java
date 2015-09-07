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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTStatement;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

public abstract class DisallowedGauvaPreconditionsCheckNotNullJavaRuleSpec {

	private DisallowedGauvaPreconditionsCheckNotNullJavaRule specimen;

	private RuleContext ruleContext;

	private ASTPackageDeclaration packageDeclaration;

	private ASTName packageName;

	private ASTImportDeclaration importDeclaration;

	private ASTName importName;

	private ASTStatement statement;

	private ASTName statementName;

	private LanguageVersion languageVersion;

	@Mock
	private LanguageVersionHandler mockLanguageVersionHandler;

	@Mock
	private Language language;

	@Mock
	private RuleViolationFactory ruleViolationFactory;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		specimen = new DisallowedGauvaPreconditionsCheckNotNullJavaRule();
		languageVersion = new LanguageVersion(language, "1.8", mockLanguageVersionHandler);
		ruleContext = new RuleContext();
		packageDeclaration = new ASTPackageDeclaration(0);
		packageName = new ASTName(1);
		importDeclaration = new ASTImportDeclaration(2);
		importName = new ASTName(3);
		statement = new ASTStatement(4);
		statementName = new ASTName(5);
		packageDeclaration.jjtAddChild(packageName, 0);
		importDeclaration.jjtAddChild(importName, 0);
		statement.jjtAddChild(statementName, 0);
		ruleContext.setLanguageVersion(languageVersion);
		when(mockLanguageVersionHandler.getRuleViolationFactory()).thenReturn(ruleViolationFactory);
	}

	protected final void givenAnImport(final String name) {
		importName.setImage(name);
	}

	protected final void givenAStatementName(final String name) {
		statementName.setImage(name);
	}

	protected final void givenAStaticImport() {
		importDeclaration.setStatic();
	}

	protected final void whenVisited() {
		specimen.visit(packageDeclaration, ruleContext);
		specimen.visit(packageName, ruleContext);
		specimen.visit(importDeclaration, ruleContext);
		specimen.visit(importName, ruleContext);
		specimen.visit(statement, ruleContext);
		specimen.visit(statementName, ruleContext);
	}

	protected final void thenViolationsWereAdded() {
		verify(ruleViolationFactory, atLeastOnce()).addViolation(eq(ruleContext), any(), any(), any(), any());
	}

	protected final void thenNoViolationsWereAdded() {
		verify(ruleViolationFactory, times(0)).addViolation(eq(ruleContext), any(), any(), any(), any());
	}
}
