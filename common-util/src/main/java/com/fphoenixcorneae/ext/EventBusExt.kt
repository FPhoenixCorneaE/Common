package com.fphoenixcorneae.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.greenrobot.eventbus.EventBus

/**
 * EventBus 安全注册方法，使用该方法注册的 EventBus 实例无需进行反注册。反注册操作在 activity
 * 销毁时会自动进行，推荐在 BaseActivity.onCreate() 方法中注册 EventBus
 *
 * @param activity 目标 activity 实例
 */
fun EventBus.safeRegister(activity: FragmentActivity) {
    if (activity.isFinishing || activity.isDestroyed) {
        loggerE("Don't register event bus on finish state or destroyed state of the activity: ${activity.javaClass.canonicalName}")
        return
    }

    activity.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_CREATE) {
                if (!isRegistered(activity)) {
                    register(activity)
                    loggerD("EventBus register success: ${source.javaClass.canonicalName}")
                }
            }

            if (event == Lifecycle.Event.ON_DESTROY) {
                if (isRegistered(activity)) {
                    unregister(activity)
                    loggerD("EventBus unregister success: ${source.javaClass.canonicalName}")
                }
            }
        }
    })
}

/**
 * EventBus 安全注册方法，使用该方法注册的 EventBus 实例无需进行反注册。反注册操作在 fragment
 * 销毁时会自动完成，推荐在 BaseFragment.onCreate() 方法中注册 EventBus
 *
 * @param fragment 目标 fragment 实例
 */
fun EventBus.safeRegister(fragment: Fragment) {
    if (fragment.isRemoving || fragment.isDetached) {
        loggerE("Don't register event bus on detach state or removing state of the activity: ${fragment.javaClass.canonicalName}")
        return
    }

    fragment.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_CREATE) {
                if (!isRegistered(fragment)) {
                    register(fragment)
                    loggerD("EventBus register success: ${source.javaClass.canonicalName}")
                }
            }

            if (event == Lifecycle.Event.ON_DESTROY) {
                if (isRegistered(fragment)) {
                    unregister(fragment)
                    loggerD("EventBus unregister success: ${source.javaClass.canonicalName}")
                }
            }
        }
    })
}

/**
 * EventBus安全注册方法，使用该方法注册的EventBus实例无需进行反注册。反注册操作在fragment
 * 销毁时会自动完成，推荐在init方法块中注册EventBus实例
 *
 * @param viewModel 目标 ViewModel 实例，ViewModel 需实现 LifecycleOwner
 */
fun EventBus.safeRegister(viewModel: LifecycleOwner) {
    LifecycleRegistry(viewModel).addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_CREATE) {
                if (!isRegistered(viewModel)) {
                    register(viewModel)
                    loggerD("EventBus register success: ${source.javaClass.canonicalName}")
                }
            }

            if (event == Lifecycle.Event.ON_DESTROY) {
                if (isRegistered(viewModel)) {
                    unregister(viewModel)
                    loggerD("EventBus unregister success: ${source.javaClass.canonicalName}")
                }
            }
        }
    })
}
