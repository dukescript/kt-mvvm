# Model View ViewModel for Kotlin

We love Kotlin. We love MVVM. This is the result.

The ViewModel:
```kotlin
class Demo : Model.Provider {
    override val objs = Model(this)

    var newTodo by observable("")
    val todos: MutableList<String> by observableList()

    val add by action {
        todos += newTodo
        newTodo = ""
    }
}
```

The View:
```html
<label for="key">Enter your name: </label>
<input id="key" type="text" data-bind="textInput: newTodo" placeholder="Enter your Name"/>
<button data-bind="click: add">add</button>
<ul class="todos" data-bind='foreach: todos' width='100%'>
    <li>
        <span data-bind="text: $data"></span> 
    </li>
</ul>    
```

## Cross platform

With kotlin-mvvm you write your application once and deploy it to *desktop*, **Android**, **iOS** and *browser*.

## Connected Applications

kotlin-mvvm offers really smooth integration with REST backends.
