package com.dukescript.api.kt.impl

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import com.dukescript.api.kt.Model
import net.java.html.BrwsrCtx
import net.java.html.json.Models
import org.netbeans.html.json.spi.Proto

internal fun impl(thiz : Model): ModelImpl {
    return thiz as ModelImpl
}

internal object Token {
}

internal final class ModelImpl constructor(private val javaObj: Any) : Model(Token) {
    internal val values: MutableList<Any?> = mutableListOf();

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
    private val computed: MutableList<(() -> Any?)?> = mutableListOf()
    private val fns: MutableList<((Any?) -> Unit)?> = mutableListOf()

    internal val proto: Proto by lazy {
        val context = BrwsrCtx.findDefault(javaObj::class.java)
        type.type.createProto(javaObj, context)
    }

    internal val type: ProtoType by lazy {
        val p = ProtoType.allModels[javaObj::class]
        p!!
    }

    internal fun addComputedProp(index: Int, fn: (() -> Any?)?, value: Any?) {
        while (computed.size <= index) {
            computed.add(null)
        }
        computed[index] = fn;
        while (values.size <= index) {
            values.add(null)
        }
        values[index] = value
    }

    internal fun addFn(index: Int, fn: ((Any?) -> Unit)?) {
        while (fns.size <= index) {
            fns.add(null)
        }
        fns[index] = fn;
    }

    internal fun getValue(prop: KProperty<*>): Any? {
        val index = type.props.indexOf(prop)
        return getValue(index)
    }

    internal fun getValue(index: Int): Any? {
        val name = type.props[index].name
        val fn = computed[index]
        val p = proto
        if (fn != null) {
            try {
                p.acquireLock(name)
                return fn()
            } finally {
                p.releaseLock()
            }
        }
        while (values.size <= index) {
            values.add(null)
        }
        p.accessProperty(name)
        return values[index];
    }

    internal fun setValue(p: KProperty<*>, value: Any?) {
        val index = type.props.indexOf(p)
        setValue(index, value)
    }

    internal fun setValue(index: Int, value: Any?) {
        val old = values[index]
        values[index] = value
        val name = type.props[index].name
        proto.valueHasMutated(name, old, value)
        val on = type.onChange[index]
        if (on != null) {
            on()
        }
    }

    internal fun invoke(index: Int, data: Any?, event: Any?) {
        val fn = fns[index]
        if (fn != null) {
            fn(data)
        }
    }

    internal fun <T> extractValue(clazz: Class<T>, value: Any?): T? {
        val obj = if (Models.isModel(clazz)) {
            proto.toModel(clazz, value)
        } else {
            value
        }
        return type.extractValue(clazz, obj)
    }

    internal fun valueHasMutated(index: Int) {
        proto.valueHasMutated(type.props[index].name, null, values[index])
        val on = type.onChange[index]
        if (on != null) {
            on()
        }
    }
}

internal class ProtoType constructor(val clazz: Class<out Model.Provider>) {
    companion object {
        public val allModels: MutableMap<Any, ProtoType> = mutableMapOf()

        public fun registerPrototype(owner: Model.Provider): ProtoType {
            val m: ProtoType? = allModels[owner::class]
            if (m == null) {
                val r = ProtoType(owner::class.java)
                allModels[owner::class] = r
                return r
            } else {
                return m
            }
        }
    }
    val props: MutableList<KProperty<*>> = mutableListOf()
    val types: MutableList<Class<*>?> = mutableListOf()
    val array: MutableList<Boolean> = mutableListOf()
    val fns: MutableList<KProperty<*>> = mutableListOf()
    val onChange: MutableList<(()->Unit)?> = mutableListOf()
    val type: Html4JavaType by lazy {
        Html4JavaType(this, clazz, props, fns)
    }
    init {
        Html4JavaType(this, clazz, mutableListOf(), mutableListOf())
    }

    fun <T> extractValue(clazz: Class<T>, value: Any?): T? {
        return type.extractValue(clazz, value)
    }

    fun addProp(
        objs: Model, isA: Boolean,
        type : Class<*>?, p: KProperty<*>,
        fn: (() -> Any?)? = null,
        init: Any? = null,
        change: (() -> Unit)? = null
    ): Int {
        var index = props.indexOf(p)
        if (index == -1) {
            props.add(p)
            types.add(type)
            array.add(isA)
            onChange.add(change)
            index = props.lastIndex
        } else {
            types[index] = type
            array[index] = isA
            onChange[index] = change;
        }
        impl(objs).addComputedProp(index, fn, init)
        return index
    }

    fun addFn(objs: Model, p: KProperty<*>, fn: ((Any?) -> Unit)?): Int {
        var index = fns.indexOf(p)
        if (index == -1) {
            fns.add(p)
            index = fns.lastIndex
        }
        impl(objs).addFn(index, fn);
        return index
    }
}

internal class Html4JavaType : Proto.Type<Model.Provider> {
    val kType: ProtoType
    constructor (
            kotlinType: ProtoType,
            type: Class<out Model.Provider>,
            props: List<KProperty<*>>,
            fns: List<KProperty<*>>
    ) : super(type, type, props.size, fns.size) {
        kType = kotlinType
        var cnt = 0
        for (p in props) {
            registerProperty(p.name, cnt++, false)
        }
        cnt = 0;
        for (f in fns) {
            registerFunction(f.name, cnt++)
        }
    }

    override fun cloneTo(model: Model.Provider?, ctx: BrwsrCtx?): Model.Provider? {
        throw UnsupportedOperationException()
    }

    override fun protoFor(obj: Any?): Proto? {
        if (obj is Model.Provider) {
            val m: Model.Provider = obj
            return impl(m.objs).proto
        }
        return null;
    }

    override fun call(model: Model.Provider, index: Int, data: Any?, event: Any?) {
        impl(model.objs).invoke(index, data, event)
    }

    public override fun getValue(model: Model.Provider, index: Int): Any? {
        return impl(model.objs).getValue(index)
    }

    override fun onChange(model: Model.Provider, index: Int) {
        impl(model.objs).valueHasMutated(index)
    }

    public override fun setValue(model: Model.Provider, index: Int, value: Any?) {
        return impl(model.objs).setValue(index, value)
    }

    override fun read(c: BrwsrCtx?, json: Any?): Model.Provider? {
        val data = this.kType.clazz.newInstance() as Model.Provider
        val propertySize = this.kType.props.size
        val names : Array<String> = Array(propertySize, { i ->
            kType.props[i].name
        })
        val tmp : Array<Any?> = arrayOfNulls(propertySize)
        val proto = impl(data.objs).proto
        proto.extract(json, names, tmp)
        val values = impl(data.objs).values
        for (index in 0 .. propertySize - 1) {
            val type = this.kType.types[index]
            if (type != null) {
                if (this.kType.array[index]) {
                    val list = if (values[index] == null) {
                        val l : MutableList<Any?> = proto.createList(names[index], index)
                        values[index] = l
                        l
                    } else {
                        val l = values[index] as MutableList<Any?>
                        l.clear()
                        l
                    }
                    if (tmp[index] != null) {
                        val arr = tmp[index] as Array<*>
                        for (elem in arr) {
                            val v = extractData(proto, type, elem)
                            list.add(v)
                        }
                    }
                } else {
                    values[index] = extractData(proto, type, tmp[index])
                }
            }
        }
        return data
    }

    private fun extractData(proto: Proto, type: Class<*>, elem: Any?): Any? {
        return if (Models.isModel(type)) {
            proto.read(type, elem)
        } else {
            extractValue(type, elem)
        }
    }


    override fun onMessage(model: Model.Provider, index: Int, type: Int, data: Any?, params: Array<Any>) {
        val clazz: Class<*> = params[0] as Class<*>
        val onSuccess: (Any) -> Unit = params[1] as (Any) -> Unit
        deliverMessage(clazz, model, index, type, data as Array<Any?>, onSuccess)
    }

    private fun <T> deliverMessage(clazz : Class<T>, model: Model.Provider, index: Int, type: Int, data: Array<Any?>, onSuccess: (List<T>) -> Unit) {
        val ctx = impl(model.objs).proto.context
        val copy : Array<Any?> = arrayOfNulls(data.size)
        impl(model.objs).type.type.copyJSON(ctx, data, clazz, copy as Array<T>)
        val list : List<T> = copy.toList()
        onSuccess(list)
    }
}


/**
* Activates providing model for given element.
* @param model the model to activate
* @param id optional id of element to activate the model at, if missing activate
*   the model to the whole UI
*/
fun applyBindings(model : Model.Provider, id : String? = null) {
    Models.applyBindings(model, id)
}

internal object ObservableP : ReadWriteProperty<Model.Provider, Any?> {
    override fun getValue(thisRef: Model.Provider, property: KProperty<*>): Any? = impl(thisRef.objs).getValue(property)
    override fun setValue(thisRef: Model.Provider, property: KProperty<*>, value: Any?): Unit = impl(thisRef.objs).setValue(property, value)
}

internal object ComputedP : ReadOnlyProperty<Model.Provider, Any?> {
    override fun getValue(thisRef: Model.Provider, property: KProperty<*>): Any? = impl(thisRef.objs).getValue(property)
}

internal class ListP<T> constructor (
    private val js : Model,
    private val name: String,
    private val index: Int,
    items : Array<out T>
): ReadOnlyProperty<Model.Provider, MutableList<T>> {
    val list : MutableList<T> by lazy {
        var list = if (impl(js).getValue(index) == null) {
            val newList = impl(js).proto.createList<T>(name, index)
            impl(js).setValue(index, newList)
            newList.addAll(items)
            newList
        } else {
            impl(js).getValue(index) as MutableList<T>
        }
        list
    }
    override fun getValue(thisRef: Model.Provider, property: KProperty<*>): MutableList<T> {
        impl(js).proto.accessProperty(name)
        return list
    }
}
