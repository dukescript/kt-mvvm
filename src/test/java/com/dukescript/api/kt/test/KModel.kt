package com.dukescript.api.kt.test

import com.dukescript.api.kt.Model
import com.dukescript.api.kt.Action
import com.dukescript.api.kt.observable
import com.dukescript.api.kt.observableList
import com.dukescript.api.kt.computed
import com.dukescript.api.kt.action
import com.dukescript.api.kt.actionWithData

import com.dukescript.api.kt.loadJSON

class TestData : Model.Provider {
    override val objs = Model(this)

    public var value: Int by observable(0)
}

class KModel(vararg historyItems: String) : Model.Provider {
    override val objs = Model(this)

    public val words: Array<String> by computed {
        val arr: Array<String> = Array(6, { "?" })
        val words: List<String> = message?.split(" ", limit = 6) ?: listOf()
        for ((index, _) in arr.withIndex()) {
            if (words.size > index) {
                arr[index] = words[index]
            }
        }
        arr
    }

    var messageChanges: Int = 0;

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

    public var message: String? by observable(null) {
        messageChanges++
    }
    public var rotating: Boolean by observable(false)
    public var data: Int by observable(0)
    public val history: MutableList<String> by observableList(*historyItems)

    val turnAnimationOn: Action by action {
        rotating = true
    }

    val turnAnimationOff: Action by action {
        rotating = false
    }

    val rotate5s: Action by action {
        rotating = true
        kotlin.concurrent.timer("disable rotation", false, 5000, 5000) {
            rotating = false
        }
    }

    val archiveMessage: Action by action {
        val msg = message
        if (msg != null) {
            history.add(msg)
        }
    }

    val popMessage: Action by action {
        if (!history.isEmpty()) {
            message = history.removeAt(history.lastIndex)
        }
    }

    val hasMessages: Boolean by computed {
        !history.isEmpty()
    }

    val canArchive: Boolean by computed {
        history.isEmpty() || history[history.lastIndex] != message
    }

    val processData: Action by actionWithData { td: TestData? ->
        val v = td?.value
        if (v != null) {
            data = v
        }
    }

    val selectWord: Action by actionWithData { word: String? ->
        message = "The word ${word} has been selected"
    }

    val showScreenSize: Action by action {
        message = "Screen size is wide"
    }

    fun loadFromPlain() {
        loadJSON("http://example.com/Hello World!", { arr: List<String> ->
            var msg = ""
            for (m in arr) {
                msg += m
                msg += " "
            }
            message = msg
        })
    }

    fun loadFromJSON() {
        loadJSON("http://example.com/message=Hello World!", { arr: List<MsgModel> ->
            message = arr[0].message
        })
    }
    fun loadFromJSONArray() {
        loadJSON("http://example.com/multi=Hello&multi=World!", { arr: List<MsgModel> ->
            var msg = ""
            for (m in arr) {
                val a2 = m.multi
                for (mm in a2) {
                    msg += mm
                    msg += " "
                }
            }
            message = msg
        })
    }
}

class MsgModel : Model.Provider {
    override val objs = Model(this)

    var message by observable("")
    val multi : MutableList<String> by observableList()
}
