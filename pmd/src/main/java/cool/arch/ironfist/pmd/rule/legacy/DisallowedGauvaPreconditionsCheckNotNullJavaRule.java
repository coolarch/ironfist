package cool.arch.ironfist.pmd.rule.legacy;

import com.google.common.base.Preconditions;

import cool.arch.ironfist.pmd.util.ClassMetadataAdapter;
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

/**
 * 
 */
public class DisallowedGauvaPreconditionsCheckNotNullJavaRule extends AbstractJavaRule {

	private static final ClassMetadataAdapter<Preconditions> ADAPTER = ClassMetadataAdapter.of(Preconditions.class);

	private static final String METHOD = "checkNotNull";

	private static final String FULL_STATIC_IMPORT = ADAPTER.asStaticImportName(METHOD);

	private static final String CLASS_AND_METHOD = ADAPTER.asImportedInvocationName(METHOD);

	private static final String FULL_STATIC_IMPORT_NAME_VIOLATION_MSG =
		"Use of static import of com.google.common.base.Preconditions.checkNotNull not allowed, please use requireNotNull instead with java.util.Objects.requireNotNull properly statically imported.";

	private static final String CLASS_IMPORT_NAME_VIOLATION_MSG =
		"Use of Preconditions.checkNotNull not allowed, please use Objects.requireNotNull instead with java.util.Objects properly imported instead.";

	private static final String METHOD_NAME_VIOLATION_MSG =
		"Use of fully qualified com.google.common.base.Preconditions.checkNotNull not allowed, please use java.util.Objects.requireNotNull instead.";

	private boolean classImportFound = false;

	private boolean staticImportFound = false;

	@Override
	public Object visit(final ASTImportDeclaration importDeclaration, final Object data) {
		if (importDeclaration.isStatic() && FULL_STATIC_IMPORT.equals(importDeclaration.getImportedName())) {
			staticImportFound = true;
		}

		if (ADAPTER.getLongName()
			.equals(importDeclaration.getImportedName())) {
			classImportFound = true;
		}

		return super.visit(importDeclaration, data);
	}

	@Override
	public Object visit(final ASTName name, final Object data) {
		final Node parent = name.jjtGetParent();

		if (!(parent instanceof ASTPackageDeclaration) && !(parent instanceof ASTImportDeclaration)) {
			if (METHOD.equals(name.getImage()) && staticImportFound) {
				addViolationWithMessage(data, name, METHOD_NAME_VIOLATION_MSG);
			}

			if (CLASS_AND_METHOD.equals(name.getImage()) && classImportFound) {
				addViolationWithMessage(data, name, CLASS_IMPORT_NAME_VIOLATION_MSG);
			}

			if (FULL_STATIC_IMPORT.equals(name.getImage())) {
				addViolationWithMessage(data, name, FULL_STATIC_IMPORT_NAME_VIOLATION_MSG);
			}
		}

		return super.visit(name, data);
	}
}
