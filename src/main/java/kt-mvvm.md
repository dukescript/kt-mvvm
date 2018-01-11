# Module kt-mvvm

Use [observable][com.dukescript.api.kt.observable], 
[observableList][com.dukescript.api.kt.observableList]
and [computed][com.dukescript.api.kt.computed] functions to define intrinsic 
well connected properties that together form [model(s)][com.dukescript.api.kt.Model]
for your user interface, network communication, etc. Use
[action][com.dukescript.api.kt.action]/[actionWithData][com.dukescript.api.kt.actionWithData] to
define handlers of interactions arriving from the user interface, network or elsewhere.

Write your application once and deploy it to *desktop*, **Android**, **iOS** and *browser*
with the help of [portable presenters](https://github.com/dukescript/dukescript-presenters#readme).

Learn more at the [kt-mvvm.org](http://kt-mvvm.org) website.

## Package com.dukescript.api.kt

The core package offering all needed to define your **MVVM** (*model-view-viewmodel*)
logic of your application. See:
* [observable][com.dukescript.api.kt.observable]
* [observableList][com.dukescript.api.kt.observableList]
* [computed][com.dukescript.api.kt.computed]
* [action][com.dukescript.api.kt.action]
* [actionWithData][com.dukescript.api.kt.actionWithData]

There is also smooth support for accessing network and performing REST or WebSocket
based operations - see [loadJSON][com.dukescript.api.kt.loadJSON] for more
details.
