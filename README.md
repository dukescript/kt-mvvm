# Model View ViewModel for Kotlin

We love Kotlin. We love MVVM. This is the result.

```kotlin
private class TwitterDemo: Model.Provider {
    override val objs = Model(this)
    var currentCustomer by observable("")
    val customers: MutableList<Tweet> by observableList()
}
```

## Cross platform

With kotlin-mvvm you write your application once and deploy it to *desktop*, **Android**, **iOS** and *browser*.

## Connected Applications

kotlin-mvvm offers really smooth integration with REST backends.
