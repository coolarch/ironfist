package cool.arch.iconfist.enforcer;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collection;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import cool.arch.iconfist.enforcer.support.DirectoryFinder;

/**
 * Maven Enforcement Plugin rule to check class membership package naming structures.
 */
public class PackageInfoPresenceRule implements EnforcerRule {

	private boolean warnOnly = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final EnforcerRuleHelper helper) throws EnforcerRuleException {
		final Log log = helper.getLog();

		try {
			final String sourceDirectoryRoot = (String) helper.evaluate("${basedir}/src/main/java");
			final String groupId = (String) helper.evaluate("${project.groupId}");
			final String packaging = (String) helper.evaluate("${project.packaging}");
			final String artifactIdPrefix = (String) helper.evaluate("${stack.artifactId.prefix}");
			final String artifactId = (String) helper.evaluate("${project.artifactId}");

			switch (packaging) {
				case "jar":
				case "war":
				case "ear":
					attemptProcessClasses(log, sourceDirectoryRoot, groupId, artifactId, artifactIdPrefix);
					break;

				default:
					log.info(MessageFormat.format("Not an applicable project type '{0}'. Skipping rule checks for {1}.", packaging, this.getClass()
						.getSimpleName()));
					break;
			}
		} catch (final ExpressionEvaluationException e) {
			throw new EnforcerRuleException(MessageFormat.format("Unable to lookup an expression: {0}", e.getLocalizedMessage()), e);
		} catch (final IOException e) {
			throw new EnforcerRuleException("Error attempting to locate directories.", e);
		}
	}

	private void attemptProcessClasses(final Log log, final String sourceDirectoryRoot, final String groupId, final String artifactId, final String artifactIdPrefix)
		throws IOException, EnforcerRuleException {
		final File srcDirectory = new File(sourceDirectoryRoot);

		if (!srcDirectory.exists()) {
			return;
		}

		final DirectoryFinder fileVisitor = new DirectoryFinder();
		final String unprefixedArtifactId = artifactId.substring(artifactIdPrefix.length());

		final StringBuilder requiredPackage = new StringBuilder(groupId).append(".")
			.append(unprefixedArtifactId.replace("-", "/"));

		final Path root = Paths.get(sourceDirectoryRoot + File.separatorChar + requiredPackage.toString()
			.replace('.', File.separatorChar));

		log.debug(MessageFormat.format("Attempting to search: {0}", root.toString()));

		Files.walkFileTree(root, fileVisitor);

		if (!fileVisitor.getRejectedPackages()
			.isEmpty()) {
			final String message = buildError(fileVisitor.getRejectedPackages());

			if (warnOnly) {
				log.warn(message);
			} else {
				throw new EnforcerRuleException(message);
			}
		}
	}

	private String buildError(final Collection<String> rejectedPackageNames) {
		final StringBuilder builder = new StringBuilder();

		builder.append("Build rejected due to the following directories not containing a package-info.java source file:");
		builder.append("\n");

		for (final String rejectedPackageName : rejectedPackageNames) {
			builder.append("\t");
			builder.append(rejectedPackageName);
			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * If your rule is cacheable, you must return a unique id when parameters or conditions
	 * change that would cause the result to be different. Multiple cached results are stored
	 * based on their id.
	 * <p>
	 * The easiest way to do this is to return a hash computed from the values of your
	 * parameters.
	 * <p>
	 * If your rule is not cacheable, then the result here is not important, you may return
	 * anything.
	 * @return the cache id
	 */
	@Override
	public String getCacheId() {
		return "";
	}

	/**
	 * This tells the system if the results are cacheable at all. Keep in mind that during
	 * forked builds and other things, a given rule may be executed more than once for the same
	 * project. This means that even things that change from project to project may still be
	 * cacheable in certain instances.
	 * @return Whether the rule is cacheable
	 */
	@Override
	public boolean isCacheable() {
		return false;
	}

	/**
	 * If the rule is cacheable and the same id is found in the cache, the stored results are
	 * passed to this method to allow double checking of the results. Most of the time this can
	 * be done by generating unique ids, but sometimes the results of objects returned by the
	 * helper need to be queried. You may for example, store certain objects in your rule and
	 * then query them later.
	 * @param rule Enforcer rule
	 * @return Whether the result is valid
	 */
	@Override
	public boolean isResultValid(final EnforcerRule rule) {
		return false;
	}
}
