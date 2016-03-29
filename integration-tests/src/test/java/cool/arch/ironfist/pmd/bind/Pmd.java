package cool.arch.ironfist.pmd.bind;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

/*
 * @formatter:off
 * cool.arch.ironfist:ironfist-integration-tests
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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
	name = "pmd")
@XmlAccessorType(FIELD)
public class Pmd {

	@XmlAttribute(
		name = "version")
	private String version;

	@XmlAttribute(
		name = "timestamp")
	private String timestamp;

	private List<File> files;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(final List<File> files) {
		this.files = files;
	}
}
