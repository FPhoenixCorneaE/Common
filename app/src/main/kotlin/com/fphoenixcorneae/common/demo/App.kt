package com.fphoenixcorneae.common.demo

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class App : Application(), ViewModelStoreOwner {

    private val mViewModelStore by lazy { ViewModelStore() }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }
}