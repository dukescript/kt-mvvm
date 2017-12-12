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

import net.java.html.lib.Array;
import net.java.html.lib.Objs;
import net.java.html.lib.Function;

class JavaScriptAccess {
    static Object apply(String name, Object thiz, Object... args) {
        Function fn = Function.$as(Objs.$as(thiz).$get(name));
        Object val = fn.apply(thiz, args);
        if (val != null && Array.$as(val).length() > 0) {
            return Array.$as(val).toArray();
        }
        return val;
    }
}
