package com.example.foodie.util

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

//LiveData deliver its update to the provided observer just once and then remove that observer immediately after delivering the update
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            //As soon as the LiveData emits a value,we remove our anonymous inner observer from the LiveData.
            //so onChanged will only be called once
            removeObserver(this)
            //After removing the inner observer, we forward the emitted value to the original observer provided to the observeOnce function
            observer.onChanged(value)
        }
    })
}

inline fun <reified T : Parcelable> Bundle.retrieveParcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}