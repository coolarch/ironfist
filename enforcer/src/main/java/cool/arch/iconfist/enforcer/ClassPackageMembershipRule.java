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

import cool.arch.iconfist.enforcer.support.ClassFinder;

/**
 * Maven Enforcement Plugin to check class membership package naming structures
 */
public class ClassPackageMembershipRule implements EnforcerRule {

	private boolean warnOnly = false;

	/**
	 * {@inheritDoc}
	 * @param helper Rule helper
	 */
	@Override
	public void execute(final EnforcerRuleHelper helper) throws EnforcerRuleException {
		final Log log = helper.getLog();

		try {
			final String target = (String) helper.evaluate("${project.build.directory}");
			final String artifactIdPrefix = (String) helper.evaluate("${stack.artifactId.prefix}");
			final String packaging = (String) helper.evaluate("${project.packaging}");
			final String groupId = (String) helper.evaluate("${project.groupId}");
			final String artifactId = (String) helper.evaluate("${project.artifactId}");

			if (artifactIdPrefix == null || artifactIdPrefix.isEmpty()) {
				throw new EnforcerRuleException("stack.artifactId.prefix must be defined.");
			}

			if (!artifactId.startsWith(artifactIdPrefix)) {
				throw new EnforcerRuleException(MessageFormat.format("ArtifactId {0} must start with {1}.", artifactId, artifactIdPrefix));
			}

			switch (packaging) {
				case "jar":
				case "war":
				case "ear":
					attemptProcessClasses(log, groupId, artifactId, target, artifactIdPrefix);
					break;

				default:
					log.info(MessageFormat.format("Not an applicable project type '{0}'. Skipping rule checks for {1}.", packaging, this.getClass()
						.getSimpleName()));
					break;
			}
		} catch (final ExpressionEvaluationException e) {
			throw new EnforcerRuleException(MessageFormat.format("Unable to lookup an expression: {0}", e.getLocalizedMessage()), e);
		} catch (final IOException e) {
			throw new EnforcerRuleException("Error attempting to locate .class files.", e);
		}
	}

	private void attemptProcessClasses(final Log log, final String groupId, final String artifactId, final String target, final String artifactIdPrefix) throws IOException, EnforcerRuleException {
		final String unprefixedArtifactId = artifactId.substring(artifactIdPrefix.length());

		final StringBuilder requiredPackage = new StringBuilder(groupId).append(".")
			.append(unprefixedArtifactId.replace("-", "."));

		final ClassFinder fileVisitor = new ClassFinder(requiredPackage.toString());

		final File targetFolder = new File(target);

		if (!targetFolder.exists()) {
			targetFolder.mkdirs();
		}

		final Path root = Paths.get(target);

		log.debug(MessageFormat.format("Attempting to search: {0}", root.toString()));

		Files.walkFileTree(root, fileVisitor);

		if (!fileVisitor.getRejectedClassNames()
			.isEmpty()) {
			final String message = buildError(requiredPackage.toString(), fileVisitor.getRejectedClassNames());

			if (warnOnly) {
				log.warn(message);
			} else {
				throw new EnforcerRuleException(message);
			}
		}
	}

	private String buildError(final String requiredPackage, final Collection<String> rejectedClassNames) {
		final StringBuilder builder = new StringBuilder();

		builder.append("Build rejected due to the following classes not being a member of the package '");
		builder.append(requiredPackage);
		builder.append("':");
		builder.append("\n");

		for (final String rejectedClassName : rejectedClassNames) {
			builder.append("\t");
			builder.append(rejectedClassName);
			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * @see EnforcerRule#getCacheId()
	 */
	@Override
	public String getCacheId() {
		return "";
	}

	/**
	 * @see EnforcerRule#isCacheable()
	 */
	@Override
	public boolean isCacheable() {
		return false;
	}

	/**
	 * @see EnforcerRule#isResultValid(EnforcerRule)
	 */
	@Override
	public boolean isResultValid(final EnforcerRule arg0) {
		return false;
	}

}
