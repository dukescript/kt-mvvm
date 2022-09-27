
package com.dukescript.api.kt.test;

/*-
 * #%L
 * %%
 * Copyright (C) 2017 Dukehoff GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DependencyCheckIT {
    @Test
    public void testParseDependencies() throws Exception {
        URL pom = DependencyCheckIT.class.getResource("/META-INF/maven/com.dukescript.api/kt-mvvm/pom.xml");
        assertNotNull("Generated pom.xml found\n" + System.getProperty("java.class.path"), pom);

        Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pom.toExternalForm());

        int cnt = 0;
        NodeList deps = dom.getElementsByTagName("dependency");
        for (int i = 0; i < deps.getLength(); i++) {
            Node n = deps.item(i);

            String groupId = find(n, "groupId");
            String artifactId = find(n, "artifactId");
            String scope = find(n, "scope");
            if ("test".equals(scope)) {
                continue;
            }
            if ("provided".equals(scope) && "org.jetbrains".equals(groupId)) {
                assertEquals("annotations", artifactId);
                continue;
            }
            if ("runtime".equals(scope)) {
                assertEquals("org.netbeans.html", groupId);
                assertEquals("ko4j", artifactId);
                continue;
            }
            if (!"org.netbeans.html".equals(groupId)) {
                assertEquals("org.jetbrains.kotlin", groupId);
                assertEquals("kotlin-stdlib", artifactId);
                continue;
            }
            cnt++;
            if ("net.java.html".equals(artifactId)) {
                continue;
            }
            if ("net.java.html.boot".equals(artifactId)) {
                continue;
            }
            if ("net.java.html.json".equals(artifactId)) {
                continue;
            }
            fail("Unexpected artifact id: " + artifactId);
        }

        assertEquals("Found 3 deps on HTML/Java", 3, cnt);
    }

    private static String find(Node n, String name) {
        NodeList children = n.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node c = children.item(i);
            if (c.getNodeName().equals(name)) {
                return c.getTextContent().trim();
            }
        }
        return null;
    }
}
