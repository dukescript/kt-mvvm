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

import java.io.IOException;
import java.io.InputStream;
import org.netbeans.html.context.spi.Contexts;
import org.netbeans.html.json.spi.JSONCall;
import org.netbeans.html.json.spi.Transfer;

public final class MockTransfer implements Transfer, Contexts.Provider {
    
    @Override
    public void extract(Object obj, String[] props, Object[] values) {
        for (String pair : obj.toString().split("&")) {
            String[] keyValue = pair.split("=");
            for (int i = 0; i < props.length; i++) {
                if (keyValue[0].equals(props[i])) {
                    if (values[i] != null) {
                        if (values[i] instanceof Object[]) {
                            Object[] arr = (Object[])values;
                            Object[] newArr = new Object[arr.length + 1];
                            System.arraycopy(arr, 0, newArr, 0, arr.length);
                            newArr[arr.length] = keyValue[i];
                            values[i] = newArr;
                        } else {
                            values[i] = new Object[] { values[i], keyValue[i] };
                        }
                    } else {
                        values[i] = keyValue[1];
                    }
                }
            }
        }
    }

    @Override
    public Object toJSON(InputStream is) throws IOException {
        throw new IOException();
    }

    @Override
    public void loadJSON(JSONCall call) {
        String url = call.composeURL(null);
        final String stringPrefix = "http://string/";
        if (url.startsWith(stringPrefix)) {
            call.notifySuccess(url.substring(stringPrefix.length()));
            return;
        }
        call.notifyError(new IllegalArgumentException(url));
    }

    @Override
    public void fillContext(Contexts.Builder bldr, Class<?> type) {
        bldr.register(Transfer.class, this, 10);
    }
    
}
