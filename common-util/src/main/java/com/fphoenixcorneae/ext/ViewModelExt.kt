package com.fphoenixcorneae.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

/**
 * 在Activity中得到Application上下文的ViewModel
 * 注意:Application需要实现ViewModelStoreOwner接口
 */
inline fun <reified T : AndroidViewModel> FragmentActivity.androidViewModel() = lazy {
    this.application.run {
        ViewModelProvider(
            this as ViewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        ).get(T::class.java)
    }
}

/**
 * 在Fragment中得到Application上下文的ViewModel
 * 注意:在fragment中调用该方法时，请在该[Fragment.onCreate]以后调用或者请用by lazy方式懒加载初始化调用，
 * 不然会提示[Fragment.requireActivity]没有导致错误
 */
inline fun <reified T : AndroidViewModel> Fragment.androidViewModel() = lazy {
    this.requireActivity().application.run {
        ViewModelProvider(
            this as ViewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        ).get(T::class.java)
    }
}