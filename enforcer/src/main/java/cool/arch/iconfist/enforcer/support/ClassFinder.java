package cool.arch.iconfist.enforcer.support;

/*
 * @formatter:off
 * cool.arch.ironfist:ironfist-enforcer
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

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.ClassReader;

/**
 * File visitor for finding class files.
 */
public class ClassFinder extends SimpleFileVisitor<Path> {

	private final String _requiredPrefix;

	private final List<String> _rejectedClassNames = new LinkedList<>();

	private static final String _class = ".class";

	/**
	 * Constructs a new {@link ClassFinder} instance.
	 * @param requiredPrefix Required prefix
	 */
	public ClassFinder(final String requiredPrefix) {
		_requiredPrefix = requireNonNull(requiredPrefix, "requiredPrefix shall not be null");
	}

	/** {@inheritDoc} */
	@Override
	public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
		return FileVisitResult.CONTINUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
		if (file.toString()
			.endsWith(_class)) {
			processClassFile(new File(file.toString()));
		}

		return FileVisitResult.CONTINUE;
	}

	private void processClassFile(final File classFile) throws IOException {
		requireNonNull(classFile, "classFile shall not be null");

		String parsedClassName = null;

		try (final InputStream inputStream = new FileInputStream(classFile)) {
			final ClassReader reader = new ClassReader(inputStream);
			parsedClassName = reader.getClassName()
				.replace("/", ".");
		}

		if (!parsedClassName.startsWith(_requiredPrefix)) {
			_rejectedClassNames.add(parsedClassName);
		}
	}

	/**
	 * Get the list of rejected class names having failed the check
	 * @return Rejected class names
	 */
	public List<String> getRejectedClassNames() {
		return _rejectedClassNames;
	}
}
