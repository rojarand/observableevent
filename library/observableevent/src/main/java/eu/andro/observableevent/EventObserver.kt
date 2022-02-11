package eu.andro.observableevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class Event<T>(val payload: T) {
    private val deliveryHistory = HashSet<String>()

    fun isDeliveredFor(eventObserverKey: String): Boolean {
        return deliveryHistory.contains(eventObserverKey)
    }

    fun markAsDeliveredFor(eventObserverKey: String) {
        deliveryHistory.add(eventObserverKey)
    }
}

open class MutableEventLiveData<T> : MutableLiveData<Event<T>>() {
    fun postEvent(value: T) {
        super.postValue(Event(value))
    }
    fun asLiveData(): LiveData<Event<T>> {
        return this
    }
}

typealias EventLiveData<T> = LiveData<Event<T>>

class EventObserver<T>(private val key: String = "", private val deliveryCallback: (t: T) -> Unit) :
    Observer<Event<T>> {

    override fun onChanged(e: Event<T>) {
        if (!e.isDeliveredFor(this.key)) {
            deliveryCallback(e.payload)
            e.markAsDeliveredFor(this.key)
        }
    }
}

class SimpleEventLiveData: MutableEventLiveData<Void?>() {
    fun fire() = postEvent(null)
}