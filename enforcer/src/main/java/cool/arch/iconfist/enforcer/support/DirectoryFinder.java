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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 * File visitor for finding directories that have package-info.java filea.
 */
public class DirectoryFinder extends SimpleFileVisitor<Path> {

	private static final String PACKAGE_INFO_FILENAME = "package-info.java";

	private final List<String> _rejectedPackageDirectories = new LinkedList<>();

	/** {@inheritDoc} */
	@Override
	public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
		final File packageInfo = new File(dir.toFile(), PACKAGE_INFO_FILENAME);

		if (!packageInfo.exists()) {
			_rejectedPackageDirectories.add(dir.toAbsolutePath()
				.toString());
		}

		return FileVisitResult.CONTINUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Get the list of rejected directory names having failed the check
	 * @return Rejected packages
	 */
	public List<String> getRejectedPackages() {
		return _rejectedPackageDirectories;
	}
}
