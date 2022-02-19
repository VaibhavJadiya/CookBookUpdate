package com.printoverit.cookbookupdate.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> LiveData<T>.observeOnce(lifecyclerOwner:LifecycleOwner,observer: Observer<T>){

    observe(lifecyclerOwner,object:Observer<T>{
        override fun onChanged(t : T?){
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}
