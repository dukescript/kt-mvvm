# Model View ViewModel for Kotlin

We love Kotlin. We love MVVM. This is the result.

The ViewModel:
```kotlin
private class HelloWorld: Model.Provider {
    override val objs = Model(this)
    var currentCustomer by observable("")
    val customers: MutableList<Tweet> by observableList()
}
```

The View:
```html
 <label for="key">Enter your name: </label>
 <input id="key" type="text" data-bind="textInput: name" placeholder="Enter your Name"/>
  <ul class="customer-list" data-bind='foreach: customers' width='100%'>
      <li>
         <span data-bind="text: $data"></span> 
      </li>
  </ul>      
    
```

## Cross platform

With kotlin-mvvm you write your application once and deploy it to *desktop*, **Android**, **iOS** and *browser*.

## Connected Applications

kotlin-mvvm offers really smooth integration with REST backends.
