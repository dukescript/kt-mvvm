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
<label for="key">Enter TODO: </label>
<input id="key" type="text" data-bind="textInput: desc"/>
<button data-bind="click: addTodo">Add</button>
<ul class="todos" data-bind='foreach: todos' width='100%'>
    <li>
        <span data-bind="text: $data"></span> 
    </li>
</ul>    
```

Clone and try the demo [here](https://github.com/dukescript/kt-mvvm-demo).

## Documentation

[Here's the KDoc](./kt-mvvm/com.dukescript.api.kt/index.html)

## Cross platform

With kotlin-mvvm you write your application once and deploy it to *desktop*, **Android**, **iOS** and *browser*.

## Connected Applications

kotlin-mvvm offers really smooth integration with REST backends.
