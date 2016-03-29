package cool.arch.ironfist.pmd;

import static org.junit.Assert.assertNotNull;

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

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cool.arch.ironfist.pmd.bind.File;
import cool.arch.ironfist.pmd.bind.Pmd;
import cool.arch.ironfist.pmd.bind.Violation;

public class ViolationValidationIT {

	@Test
	public void validateViolationsFile() throws Exception {
		final Path path = Paths.get(System.getProperty("user.dir"), "target", "pmd.xml");
		final JAXBContext jaxbContext = JAXBContext.newInstance(Pmd.class, File.class, Violation.class);
		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		final Pmd pmd = (Pmd) unmarshaller.unmarshal(path.toFile());

		assertNotNull("Root pmd object shall not be null", pmd);
		assertNotNull("version shall not be null", pmd.getVersion());
		assertNotNull("timestamp shall not be null", pmd.getTimestamp());
	}

	private void placeholder(final Path path) throws Exception {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		final Document document = dBuilder.parse(path.toFile());
		document.getDocumentElement()
			.normalize();

		final NodeList nodes = document.getElementsByTagName("violation");

		for (int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final Element element = (Element) node;

				System.out.println("Staff id : " + element.getAttribute("id"));
			}
		}
	}
}
