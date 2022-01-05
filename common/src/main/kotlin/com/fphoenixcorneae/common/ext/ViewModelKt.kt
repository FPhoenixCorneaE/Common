package com.fphoenixcorneae.common.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 在 Activity 中得到 Application 上下文的 ViewModel
 * 注意: Application 需要实现 ViewModelStoreOwner 接口.
 */
inline fun <reified VM : ViewModel> FragmentActivity.androidViewModel() = lazy {
    this.application.run {
        ViewModelProvider(
            this as ViewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        ).get(VM::class.java)
    }
}

/**
 * 在 Fragment 中得到 Application 上下文的 ViewModel
 * 注意:
 *     1.Application 需要实现 ViewModelStoreOwner 接口.
 *     2.在 fragment 中调用该方法时，请在该[Fragment.onCreate]以后调用或者请用 by lazy 方式懒加载初始化调用，
 * 不然会提示[Fragment.requireActivity]没有导致错误.
 */
inline fun <reified VM : ViewModel> Fragment.androidViewModel() = lazy {
    this.requireActivity().application.run {
        ViewModelProvider(
            this as ViewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        ).get(VM::class.java)
    }
}