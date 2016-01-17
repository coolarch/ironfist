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

import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import net.sourceforge.pmd.lang.ast.AbstractNode;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;

/**
 * 
 */
public class DisallowedGauvaPreconditionsCheckNotNullJavaRule extends AbstractJavaRule {

	private static final String METHOD = "checkNotNull";

	private static final String FULL_NAME = "com.google.common.base.Preconditions";

	private static final String FULL_STATIC_IMPORT = "com.google.common.base.Preconditions.checkNotNull";

	private static final String CLASS_AND_METHOD = "Preconditions.checkNotNull";

	private static final String FULL_STATIC_IMPORT_NAME_VIOLATION_MSG =
		"Use of static import of com.google.common.base.Preconditions.checkNotNull not allowed, please use requireNotNull instead with java.util.Objects.requireNotNull properly statically imported.";

	private static final String CLASS_IMPORT_NAME_VIOLATION_MSG = "Use of Preconditions.checkNotNull not allowed, please use Objects.requireNotNull with java.util.Objects properly imported instead.";

	private static final String METHOD_NAME_VIOLATION_MSG =
		"Use of fully qualified com.google.common.base.Preconditions.checkNotNull not allowed, please use java.util.Objects.requireNotNull instead.";

	private boolean classImportFound = false;

	private boolean staticImportFound = false;

	private final Map<String, BiConsumer<ASTName, Object>> handlers = new HashMap<>();

	public DisallowedGauvaPreconditionsCheckNotNullJavaRule() {
		handlers.put(METHOD, (name, data) -> {
			if (staticImportFound) {
				addViolationWithMessage(data, name, METHOD_NAME_VIOLATION_MSG);
			}
		});
		;

		handlers.put(CLASS_AND_METHOD, (name, data) -> {
			if (classImportFound) {
				addViolationWithMessage(data, name, CLASS_IMPORT_NAME_VIOLATION_MSG);
			}
		});

		handlers.put(FULL_STATIC_IMPORT, (name, data) -> addViolationWithMessage(data, name, FULL_STATIC_IMPORT_NAME_VIOLATION_MSG));
	}

	@Override
	public Object visit(final ASTImportDeclaration importDeclaration, final Object data) {
		if (importDeclaration.isStatic() && FULL_STATIC_IMPORT.equals(importDeclaration.getImportedName())) {
			staticImportFound = true;
		}

		if (FULL_NAME.equals(importDeclaration.getImportedName())) {
			classImportFound = true;
		}

		return super.visit(importDeclaration, data);
	}

	@Override
	public Object visit(final ASTName name, final Object data) {
		Optional.ofNullable(name)
			.filter(n -> !(n.jjtGetParent() instanceof ASTPackageDeclaration))
			.filter(n -> !(n.jjtGetParent() instanceof ASTImportDeclaration))
			.map(AbstractNode::getImage)
			.map(handlers::get)
			.ifPresent(c -> c.accept(name, data));

		return super.visit(name, data);
	}
}
