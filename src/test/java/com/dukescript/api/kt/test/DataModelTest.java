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

import net.java.html.json.Models;
import net.java.html.junit.BrowserRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(BrowserRunner.class)
public class DataModelTest {
    @Test public void propertyCanBeAssociated() {
        KModel model = new KModel(new String[0]);
        assertEmptyMessage(model);
        model.setMessage("Hello World!");
        assertEquals("Hello World!", model.getMessage());
    }

    @Test public void propertyCanBeComputed() {
        KModel model = new KModel(new String[0]);
        assertEmptyMessage(model);
        assertEquals("No message changes yet", 0, model.getMessageChanges());
        model.setMessage("Hello World!");
        assertEquals("One message change", 1, model.getMessageChanges());
        String[] arr = model.getWords();
        assertEquals(6, arr.length);
        assertEquals("Hello", arr[0]);
        assertEquals("World!", arr[1]);
        assertEquals("?", arr[2]);
        assertEquals("?", arr[3]);
        assertEquals("?", arr[4]);
        assertEquals("?", arr[5]);
    }

    @Test public void worksAndInitializeMessage() {
        KModel model = new KModel(new String[0]);
        String[] arr = assertEmptyMessage(model);
        assertEquals("?", arr[1]);
        assertEquals("?", arr[2]);
        assertEquals("?", arr[3]);
        assertEquals("?", arr[4]);
        assertEquals("?", arr[5]);
    }

    @Test public void invokeAFunction() {
        KModel model = new KModel(new String[0]);
        assertEmptyMessage(model);
        Object raw = Models.toRaw(model);
        Object res = JavaScriptAccess.apply("turnAnimationOn", raw);
        assertNull("No return value", res);
        assertTrue("Now rotating", model.getRotating());
    }

    @Test public void invokeAFunctionWithModelParameter() {
        TestData td = new TestData();
        td.setValue(42);
        KModel model = new KModel(new String[0]);
        assertEmptyMessage(model);
        Object rawData = Models.toRaw(td);
        Object raw = Models.toRaw(model);
        Object res = JavaScriptAccess.apply("processData", raw, rawData);
        assertNull("No return value", res);
        assertEquals("Received the data", (int) 42, (int) model.getData());
    }

    @Test public void invokeAFunctionWithStringParameter() {
        KModel model = new KModel(new String[0]);
        assertEmptyMessage(model);
        Object rawData = "Nazdar";
        Object raw = Models.toRaw(model);
        assertEquals("No message changes yet", 0, model.getMessageChanges());
        Object res = JavaScriptAccess.apply("selectWord", raw, rawData);
        assertNull("No return value", res);
        assertTrue("Message changed: " + model.getMessage(), model.getMessage().contains("Nazdar"));
        assertEquals("One message change", 1, model.getMessageChanges());
    }

    @Test public void changingHistory() {
        KModel model = new KModel(new String[0]);
        assertEmptyMessage(model);
        assertFalse("No messages yet", model.getHasMessages());
        model.setMessage("test message 1");
        assertTrue("Possible to archive", model.getCanArchive());

        Object raw = Models.toRaw(model);
        Object res = JavaScriptAccess.apply("archiveMessage", raw);
        assertNull("No return value", res);

        assertTrue("Now we have messages", model.getHasMessages());
        assertFalse("Cannot archive now", model.getCanArchive());
    }

    @Test
    public void initialHistory() {
        KModel model = new KModel(new String[] { "History1", "History2" });
        assertTrue("Cannot archive now", model.getCanArchive());
        assertEquals(2, model.getHistory().size());
        assertEquals("History1", model.getHistory().get(0));
        assertEquals("History2", model.getHistory().get(1));

        Object raw = Models.toRaw(model);
        Object res = JavaScriptAccess.apply("history", raw);
        assertNotNull("Some return value", res);
        assertTrue("It is an array", res instanceof Object[]);
        Object[] arr = (Object[]) res;
        assertEquals("Array of size two", 2, arr.length);
        assertEquals("History1", arr[0]);
        assertEquals("History2", arr[1]);
    }

    @Test
    public void loadStringFromURL() {
        KModel model = new KModel(new String[0]);
        model.loadFromPlain();
        assertEquals("Hello World! ", model.getMessage());
    }

    @Test
    public void loadJSONWithStringFromURL() {
        KModel model = new KModel(new String[0]);
        model.loadFromJSON();
        assertEquals("Hello World!", model.getMessage());
    }

    @Test
    public void loadJSONWithStringArrayFromURL() {
        KModel model = new KModel(new String[0]);
        model.loadFromJSONArray();
        assertEquals("Hello World! ", model.getMessage());
    }

    private String[] assertEmptyMessage(KModel model) {
        String empty = model.getMessage();
        assertEquals("No message specified yet", null, empty);
        String[] arr = model.getWords();
        assertEquals(6, arr.length);
        assertEquals("No words provided yet", "?", arr[0]);
        return arr;
    }

}
