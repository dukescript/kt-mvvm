# Model View ViewModel for Kotlin

We love Kotlin. We love MVVM. This is the result.

The ViewModel:
```kotlin
class Demo : Model.Provider {
    override val objs = Model(this)

    var desc by observable("")
    val todos: MutableList<String> by observableList()

    val addTodo by action {
        todos += desc
        desc = ""
    }
}
```

The View:
```html
<label for="key">New Todo:</label>
<input id="key" type="text" data-bind="textInput: desc"/>
<button data-bind="click: addTodo">Add</button>
<ul class="todos" data-bind='foreach: todos'>
    <li>
        <span data-bind="text: $data"></span> 
    </li>
</ul>    
```

## Getting Started

Clone and try the demo [here](https://github.com/dukescript/kt-mvvm-demo):
```bash
$ git clone https://github.com/dukescript/kt-mvvm-demo.git yourprj
$ cd yourprj
$ mvn package exec:exec
```
then edit the application logic in `src/main/java/com/kt/mvvm/demo/Demo.kt`
or the view in `src/main/webapp/pages/index.html`. 
Run the `mvn package exec:exec` again to see your changes.

## Documentation

Use [observable](./kt-mvvm/com.dukescript.api.kt/observable.html), 
[observableList](./kt-mvvm/com.dukescript.api.kt/observable-list.html)
and [computed](./kt-mvvm/com.dukescript.api.kt/computed.html) functions to define intrinsic 
well connected properties that together form [model(s)](./kt-mvvm/com.dukescript.api.kt/-model.html)
for your user interface, network communication, etc. Use
[action](./kt-mvvm/com.dukescript.api.kt/action.html)/[actionWithData](./kt-mvvm/com.dukescript.api.kt/action-with-data.html) to
define handlers of interactions arriving from the user interface, network or elsewhere.

Get the overall picture at the [KDoc](./kt-mvvm/com.dukescript.api.kt/index.html) for
the whole [Kotlin MVVM API](./kt-mvvm/com.dukescript.api.kt/index.html).

## Cross platform

With kotlin-mvvm you write your application once and deploy it to *desktop*, **Android**, **iOS** and *browser*
with the help of [portable presenters](https://github.com/dukescript/dukescript-presenters#readme).

## Connected Applications

kotlin-mvvm offers really smooth integration with REST backends - see
[loadJSON](./kt-mvvm/com.dukescript.api.kt/load-j-s-o-n.html) for more details.
