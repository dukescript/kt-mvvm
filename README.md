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

Clone the demo [repository](https://github.com/dukescript/kt-mvvm-demo)
to give **Kotlin** and **MVVM** a try:
```bash
$ git clone https://github.com/dukescript/kt-mvvm-demo.git
$ cd kt-mvvm-demo
$ ./gradlew run # or: mvn package exec:exec
```
then edit the application logic or the application UI as
```bash
$ open src/main/java/com/kt/mvvm/demo/Demo.kt
$ open src/main/webapp/pages/index.html
```
and build and execute your application again to see outcome of your changes:
```bash
$ ./gradlew run
# or if you want to use Maven invoke:
$ mvn package exec:exec
```
Repeat until your application becomes as slick as needed!

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

Our bindings for the HTML-based view are documented [here](/bindings/bindings.md).

## Cross platform

With `kt-mvvm` you write your application once and deploy it to *desktop*, [Android](android.md) , [iOS](ios.md) and [browser](browser.md)
with the help of [portable presenters](https://github.com/dukescript/dukescript-presenters#readme).

## Connected Applications

`kt-mvvm` offers really smooth integration with REST backends - see
[loadJSON](./kt-mvvm/com.dukescript.api.kt/load-j-s-o-n.html) for more details.

## License

The `kt-mvvm` API is [licensed](https://github.com/dukescript/kt-mvvm/blob/master/LICENSE.md)
under *Apache 2.0* license. The default **JavaFX WebView** based
[presenter](https://github.com/apache/incubator-netbeans-html4j/#readme)
comes directly from an [Apache project](https://github.com/apache/incubator-netbeans-html4j/).
As such one can have fully functional system covered by benevolent *Apache license*.

[Alternative presenters](https://github.com/dukescript/dukescript-presenters#readme)
or [rendering environments](https://github.com/jtulach/bck2brwsr) come with their
own licensing terms.

Commercial support for the overall ecosystem is available via the
[DukeScript project](http://dukescript.com/#support) channels.

