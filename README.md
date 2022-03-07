## ObservableEvent
`ObservableEvent` is the missing piece of code in the Android MVVM code base. Thanks to `ObservableEvent` you can easly process events only once.

### Example

Let say that you present errors in the following manner:

```kotlin
//Sending
class MyViewModel(): ViewModel() {
...
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    
    fun doSomething() {
        try {
            ...
        } catch (e: Exception) {
            _errorMessage.post(e.message)
        }
    }
}
...

//Receiving
class MyActivity : AppCompatActivity() {
...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ...
        viewModel.errorMessage.observe(this, Observer { message ->
            showAlert(message)
        })
        ...
    }
}
```
In case an error was observed and the activity is recreated, an alert window is shown the second time. This is unwanted behaviour. The error happened once, so the alert window should be shown once too.

With `ObservableEvent` an Observer **receives data only once per mutable data update**.

### Installation

1. In root `build.gradle` (project level), ensure jitpack dependency exists
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2. In app `build.gradle` (module level), add the dependency
```
dependencies {
    implementation 'com.github.rojarand:observableevent:0.1.0'
}
```

### Usage

```kotlin
//Sending
class MyViewModel(): ViewModel() {
...
    private val _errorMessage = MutableEventLiveData<String>()
    val errorMessage: EventLiveData<String>
        get() = _errorMessage
    
    fun doSomething() {
        try {
            ...
        } catch (e: Exception) {
            _errorMessage.post(e.message)
        }
    }
...
}

//Receiving
class MyActivity : AppCompatActivity() {
...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ...
        //Note!!! We use EventObserver here
        viewModel.errorMessage.observe(this, EventObserver { message ->
            showAlert(message)
        })
        ...
    }
}
```

If you want have a `void` event live data without specifying an event class you can use `SimpleEventLiveData` class. `SimpleEventLiveData` does not carry any data.

```kotlin
//ViewModel
private val _agreementAcceptationNeeded = SimpleEventLiveData()
val agreementAcceptationNeeded = _agreementAcceptationNeeded.asLiveData()
fun checkIfAgreementAccepted() {
    if (!agreementAccepted) {
        _agreementAcceptationNeeded.fire()
    }
}

//View
viewModel.agreementAcceptationNeeded.observe(viewLifecycleOwner, EventObserver {
    showAcceptationNeededMessage()
})
```

### License
MIT © [Robert Andrzejczyk](https://github.com/rojarand)

