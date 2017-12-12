@file:JvmName("Models")

package com.dukescript.api.kt

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import net.java.html.json.Models
import com.dukescript.api.kt.impl.*

/** Defines an observable property.
* @param initialValue inital value of the property
* @param onChange code to invoke whenever the value of the property changes
* @sample com.dukescript.api.kt.test.KModel.message
* @sample com.dukescript.api.kt.test.KModel.rotating
*/
inline fun <reified T> Model.Provider.observable(initialValue: T, noinline onChange: (() -> Unit)? = null): Model.Writable<T> {
    return this.objs.observable(T::class.java, initialValue, onChange);

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
}

/** Defines an observable array property.
* @param items optional elements to fill the array with
*/
inline fun <reified T> Model.Provider.observableList(vararg items: T, noinline onChange: (() -> Unit)? = null): Model.Readable<MutableList<T>> {
    var arr : Array<out T> = items
    return this.objs.observableList(T::class.java, arr, onChange);
}

/** Defines derived, computed property. Its value depends
* on other properties and is recomputed when those dependant
* properties change.
* @sample com.dukescript.api.kt.test.KModel.message
* @sample com.dukescript.api.kt.test.KModel.words
*
*/
fun <T> Model.Provider.computed(fn : () -> T): Model.Readable<T> {
    return Model.Readable<T> { js: Model.Provider, prop : KProperty<*> ->
        ProtoType.registerPrototype(js).addProp(js.objs, false, null, prop, fn = fn as (() -> Any))
        ComputedP as ReadOnlyProperty<Model.Provider, T>
    }
}

/** Creates a handler to react on action happening
* in the user interface.
* @sample com.dukescript.api.kt.test.KModel.rotating
* @sample com.dukescript.api.kt.test.KModel.turnAnimationOn
*/
fun Model.Provider.action(f: () -> Unit): Model.Readable<Action> {
    val fn: (Any?) -> Unit = { _ -> f() }
    return Model.Readable<Action> { js, prop ->
        ProtoType.registerPrototype(js).addFn(js.objs, prop, fn)
        ActionProperty(Action(Token))
    }
}

/** Creates a handler to react on action happening
* in the user interface and provide identification of
* the interface element that triggered the action.
*
* @sample com.dukescript.api.kt.test.KModel.message
* @sample com.dukescript.api.kt.test.KModel.selectWord
*
* @param f one argument function to handle the operation
*/
inline fun <reified T> Model.Provider.actionWithData(noinline f: (T?) -> Unit): Model.Readable<Action> {
    return this.actionWithData(T::class.java, f)
}

/** Creates a handler to react on action happening
* in the user interface and provide identification of
* the interface element that triggered the action.
* @param type the type of the interface element the function expects
* @param f one argument function to handle the operation
*/
fun <T> Model.Provider.actionWithData(type: Class<T>, f: (T?) -> Unit): Model.Readable<Action> {
    val fn: (Any?) -> Unit = { raw ->
        val value: T? = (this.objs as ModelImpl).extractValue(type, raw)
        f(value)
    }
    return Model.Readable<Action> { js, prop ->
        ProtoType.registerPrototype(js).addFn(js.objs, prop, fn)
        ActionProperty(Action(Token))
    }
}

/** Represents an operation callable from the user interface.
* @sample com.dukescript.api.kt.test.KModel.message
* @sample com.dukescript.api.kt.test.KModel.showScreenSize
* @sample com.dukescript.api.kt.test.KModel.selectWord
*/
final class Action internal constructor (token : Token) {
}

private class ActionProperty constructor (val action : Action) : ReadOnlyProperty<Model.Provider, Action>  {
    override fun getValue(thisRef: Model.Provider, property: KProperty<*>): Action = action
}

/** Instantiates new [Model] associated with provided `javaObj` and
 * holding all the necessary data for communication with JavaScript.
 * If you have an object that you want to mirror in the JavaScript side,
 * create a class and implement [Model.Provider] - it has just a single
 * read only property - implement it by instantiating the [Model]
 * instance with a pointer to your object.
 *
 * @sample com.dukescript.api.kt.test.TestData
 */
public fun Model(thiz: Model.Provider): Model = ModelImpl(thiz)

/** Holds internal data associated with provided kotlin object. Create using
 * the `Model` factory method:
 *
 * @sample com.dukescript.api.kt.test.TestData
 */
public open class Model internal constructor(token : Token) {
    /** Helper method. Called from [com.dukescript.api.kt.observable].
     */
    public fun <T> observable(type: Class<T>, initialValue: T, onChange: (() -> Unit)?): Model.Writable<T> {
        return Model.Writable<T> { js: Model.Provider, prop : KProperty<*> ->
            ProtoType.registerPrototype(js).addProp(js.objs, false, type, prop, init = initialValue, change = onChange)
            ObservableP as ReadWriteProperty<Model.Provider, T>
        }
    }

    /** Helper method. Called from [com.dukescript.api.kt.observableList].
     */
    public fun <T> observableList(type: Class<T>, items: Array<out T>, onChange: (() -> Unit)?): Model.Readable<MutableList<T>> {
        return Model.Readable<MutableList<T>> { js: Model.Provider, prop : KProperty<*> ->
            val index = ProtoType.registerPrototype(js).addProp(js.objs, true, type, prop, change = onChange)
            ListP(js.objs, prop.name, index, items)
        }
    }

    /** Interface to implement by objects that willing to expose its properties
     * (e.g. [com.dukescript.api.kt.observable], 
     * [com.dukescript.api.kt.observableList] or [com.dukescript.api.kt.computed])
     * as [Model]s. Trivial implementation can look like this:
     *
     * @sample com.dukescript.api.kt.test.TestData
     */
    public interface Provider {
        /** The property to fill with [Model] instance associated your
         * object.
         */
        val objs: Model;
    }

    /** Delegate for creating read-write property.
    */
    public final class Writable<T> constructor (
        private val factory : (Model.Provider, KProperty<*>) -> ReadWriteProperty<Model.Provider, T>
    ) {
        /** Creates read/write property.
         */
        operator fun provideDelegate(
            thisRef: Model.Provider, prop: KProperty<*>
        ): ReadWriteProperty<Model.Provider, T> {
            return factory(thisRef, prop)
        }
    }

    /** Delegate for creating read-only property.
    */
    public final class Readable<T> internal constructor (
        private val factory : (Model.Provider, KProperty<*>) -> ReadOnlyProperty<Model.Provider, T>
    ) {
        /** Creates read only property.
         */
        operator fun provideDelegate(
                thisRef: Model.Provider,
                prop: KProperty<*>
        ): ReadOnlyProperty<Model.Provider, T> {
            return factory(thisRef, prop)
        }
    }
}

/** Activates given [Model] in the current context.
 * @param model class with [observable] or [computed] properties and [actions][action]
 * @param id optional id of a UI element to apply the model to
 */
public fun applyBindings(model : Model.Provider, id : String? = null) {
    Models.applyBindings(model, id)
}
