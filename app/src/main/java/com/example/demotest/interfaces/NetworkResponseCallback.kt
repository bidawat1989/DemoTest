package com.example.demotest.interfaces

interface NetworkResponseCallback {
    fun onNetworkSuccess()
    fun onNetworkFailure(th : Throwable)
}