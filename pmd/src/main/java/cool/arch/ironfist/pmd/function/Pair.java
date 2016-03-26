package cool.arch.ironfist.pmd.function;

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

/**
 * Arity 2 tuple.
 * @param <T> Type of value 0
 * @param <U> Type of value 1
 */
public class Pair<T, U> {

	private final T value0;

	private final U value1;

	private Pair(final T value0, final U value1) {
		this.value0 = value0;
		this.value1 = value1;
	}

	/**
	 * @return the value0
	 */
	public final T getValue0() {
		return value0;
	}

	/**
	 * @return the value1
	 */
	public final U getValue1() {
		return value1;
	}

	/**
	 * Creates a new Pair instance for values value0 and value1.
	 * @param value0 First value
	 * @param value1 Second value
	 * @return Pair instance
	 */
	public static <T, U> Pair<T, U> of(final T value0, final U value1) {
		return new Pair<>(value0, value1);
	}
}
