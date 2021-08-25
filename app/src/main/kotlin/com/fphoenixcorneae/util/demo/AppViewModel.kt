package com.fphoenixcorneae.util.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {

    private val _global = MutableLiveData<String>()
    val global: LiveData<String>
        get() = _global

    fun setGlobalString(global: String) {
        _global.postValue(global)
    }
}