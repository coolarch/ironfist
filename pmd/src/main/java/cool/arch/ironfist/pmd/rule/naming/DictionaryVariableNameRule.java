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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;

/**
 * PMD rule for verifying that variable name segments are dictionary words.
 */
public class DictionaryVariableNameRule extends AbstractJavaRule {

	public static final String DICTIONARY_RESOURCE_PATH = "/" + DictionaryVariableNameRule.class.getPackage()
		.getName()
		.replace(".", "/") + "/" + "valid.dic";

	enum Checker {
			INSTANCE;

		public boolean containsWord(final String word) {
			return spellChecker.isCorrect(word);
		}

		private final SpellChecker spellChecker;

		private Checker() {
			spellChecker = buildSpellChecker();
		}

		private SpellChecker buildSpellChecker() {
			SpellChecker checker = null;

			try (final InputStream inputStream = getClass().getResourceAsStream(DICTIONARY_RESOURCE_PATH); final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
				final SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap(reader);
				checker = new SpellChecker(dictionary);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}

			return checker;
		}
	}

	@Override
	public Object visit(final ASTVariableDeclaratorId node, final Object data) {
		final String variableName = node.getImage()
			.toString();

		if (!onlyContainsValidWords(variableName)) {
			addViolation(data, node);
		}

		return super.visit(node, data);
	}

	private boolean onlyContainsValidWords(final String variableName) {
		final String regex = "([A-Z][a-z]+)";
		final String replacement = "_$1";
		final String[] words = variableName.replaceAll(regex, replacement)
			.replace('_', ' ')
			.trim()
			.toLowerCase()
			.split(" ");

		boolean validity = true;

		for (final String word : words) {
			if (!Checker.INSTANCE.containsWord(word)) {
				validity = false;
				break;
			}
		}

		return validity;
	}
}
