@file:JvmName("Net")

package com.dukescript.api.kt


inline fun <reified T> Model.Provider.loadJSON(
    baseUrl: String, noinline onSuccess: (List<T>) -> Unit,
    noinline onError: ((kotlin.Throwable) -> Unit)? = null,
    method : String = "GET", data : Any? = null,
    headers : Map<String,String>? = null, afterUrl: String? = null
) {
    loadJSON(T::class.java, baseUrl, onSuccess, onError, method, data, headers, afterUrl)
}

fun <T> Model.Provider.loadJSON(
    clazz: Class<T>,
    baseUrl: String, onSuccess: (List<T>) -> Unit,
    onError: ((kotlin.Throwable) -> Unit)?,
    method : String, data : Any?,
    headers : Map<String,String>?, afterUrl: String?
) {
    val headerText = if (headers != null) {
        var t = ""
        for (entry in headers.entries) {
            t += "${entry.key}: ${entry.value}"
        }
        t
    } else {
        ""
    }
    var impl = this.objs as com.dukescript.api.kt.impl.ModelImpl;

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
    impl.proto.loadJSONWithHeaders(0, headerText, baseUrl, afterUrl, method, data, clazz, onSuccess, onError)
}

