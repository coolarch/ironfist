package cool.arch.ironfist.pmd;

import java.util.concurrent.atomic.AtomicBoolean;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;

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

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class DisallowedGauvaPreconditionsCheckNotNullJavaRule extends AbstractJavaRule {

	public static final String FULL_STATIC_IMPORT_NAME = "java.util.Objects.requireNonNull";

	public static final String IMPORT_NAME = "java.util.Objects";

	public static final String CLASS_AND_METHOD_NAME = "Objects.requireNonNull";

	public static final String METHOD_NAME = "requireNonNull";

	public static final String CLASS_IMPORT_FOUND_KEY = "DisallowedGauvaPreconditionsCheckNotNullJavaRule.CLASS_IMPORT_FOUND";

	public static final String STATIC_IMPORT_FOUND_KEY = "DisallowedGauvaPreconditionsCheckNotNullJavaRule.STATIC_IMPORT_FOUND";

	@Override
	public void start(RuleContext ctx) {
		ctx.setAttribute(CLASS_IMPORT_FOUND_KEY, new AtomicBoolean(false));
		ctx.setAttribute(STATIC_IMPORT_FOUND_KEY, new AtomicBoolean(false));
		super.start(ctx);
	}

	@Override
	public Object visit(ASTImportDeclaration importDeclaration, Object data) {

		System.out.println("Import: " + importDeclaration.getImportedName());

		final RuleContext context = (RuleContext) data;
		final AtomicBoolean classImportFound = (AtomicBoolean) context.getAttribute(CLASS_IMPORT_FOUND_KEY);
		final AtomicBoolean staticImportFound = (AtomicBoolean) context.getAttribute(STATIC_IMPORT_FOUND_KEY);

		if (importDeclaration.isStatic() && FULL_STATIC_IMPORT_NAME.equals(importDeclaration.getImportedName())) {
			staticImportFound.set(true);
		}

		if (IMPORT_NAME.equals(importDeclaration.getImportedName())) {
			classImportFound.set(true);
		}

		return super.visit(importDeclaration, data);
	}

	@Override
	public Object visit(ASTName name, Object data) {
		final RuleContext context = (RuleContext) data;
		final AtomicBoolean classImportFound = (AtomicBoolean) context.getAttribute(CLASS_IMPORT_FOUND_KEY);
		final AtomicBoolean staticImportFound = (AtomicBoolean) context.getAttribute(STATIC_IMPORT_FOUND_KEY);
		final Node parent = name.jjtGetParent();

		if (parent instanceof ASTPackageDeclaration) {
			// Intentionally ignore
		} else if (parent instanceof ASTImportDeclaration) {
			// Intentionally ignore
		} else {
			if (METHOD_NAME.equals(name.getImage()) && staticImportFound.get()) {
				addViolationWithMessage(data, name, "Use of fully qualified " + METHOD_NAME + " not allowed, please use java.util.Objects.requireNonNull instead.");
			}

			if (CLASS_AND_METHOD_NAME.equals(name.getImage()) && classImportFound.get()) {
				addViolationWithMessage(data, name, "Use of " + CLASS_AND_METHOD_NAME + " not allowed, please use Objects.requireNonNull instead with java.util.Objects properly imported instead.");
			}

			if (FULL_STATIC_IMPORT_NAME.equals(name.getImage())) {
				addViolationWithMessage(data, name, "Use of static import of " + FULL_STATIC_IMPORT_NAME
					+ " not allowed, please use requireNonNull instead with java.util.Objects.requireNonNull properly statically imported instead.");
			}
		}

		return super.visit(name, data);
	}
}
