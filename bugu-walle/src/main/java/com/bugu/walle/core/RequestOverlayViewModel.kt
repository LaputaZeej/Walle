package com.bugu.walle.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RequestOverlayViewModel : ViewModel() {
    private val _success: MutableLiveData<Boolean> = MutableLiveData()
    val success: LiveData<Boolean> = _success

    fun result(success: Boolean) {
        _success.value = success
    }
}