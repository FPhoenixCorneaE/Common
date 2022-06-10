package com.fphoenixcorneae.common.ext

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.*

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> Fragment.newInstanceFragment(
    args: Bundle? = null,
    vararg pair: Pair<String, String>
): T {
    return requireContext().newInstanceFragment(args, *pair)
}

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> Context.newInstanceFragment(
    args: Bundle? = null,
    vararg pair: Pair<String, String>
): T {
    val className = T::class.java.name
    val clazz = FragmentFactory.loadFragmentClass(classLoader, className)
    val f = clazz.getConstructor().newInstance()
    (args ?: Bundle()).also {
        pair.forEach { arg ->
            it.putString(arg.first, arg.second)
        }
        it.classLoader = f.javaClass.classLoader
        f.arguments = it
    }
    return f as T
}


inline fun FragmentManager.inTransaction(
    func: FragmentTransaction.() -> Unit
): Boolean = run {
    beginTransaction().apply { func() }.commitAllowingStateLoss()
    executePendingTransactions()
}


//==================================================Fragment======================================================//
@SuppressLint("ObsoleteSdkInt")
fun Fragment.addFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    @AnimatorRes @AnimRes enterAnim: Int = 0,
    @AnimatorRes @AnimRes exitAnim: Int = 0,
    @AnimatorRes @AnimRes popEnterAnim: Int = 0,
    @AnimatorRes @AnimRes popExitAnim: Int = 0,
    vararg sharedElement: View
) = childFragmentManager.inTransaction {
    if (addToBackStack) {
        addToBackStack(fragment.javaClass.name)
    }
    setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElement.forEach { view ->
            addSharedElement(view, view.transitionName)
        }
    }
    if (!fragment.isAdded) {
        add(containerViewId, fragment, fragment.javaClass.name)
    }
}

@SuppressLint("ObsoleteSdkInt")
fun Fragment.hideShowFragment(
    @IdRes containerViewId: Int,
    newFragment: Fragment,
    previousFragment: Fragment? = null,
    addToBackStack: Boolean = false,
    @AnimatorRes @AnimRes enterAnim: Int = 0,
    @AnimatorRes @AnimRes exitAnim: Int = 0,
    @AnimatorRes @AnimRes popEnterAnim: Int = 0,
    @AnimatorRes @AnimRes popExitAnim: Int = 0,
    vararg sharedElement: View
) = childFragmentManager.inTransaction {
    if (addToBackStack) {
        addToBackStack(newFragment.javaClass.name)
    }
    setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElement.forEach { view ->
            addSharedElement(view, view.transitionName)
        }
    }
    if (previousFragment != null) {
        hide(previousFragment)
    }
    if (newFragment.isAdded) {
        show(newFragment)
    } else {
        add(containerViewId, newFragment, newFragment.javaClass.name)
        show(newFragment)
    }
}

@SuppressLint("ObsoleteSdkInt")
fun Fragment.replaceFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    @AnimatorRes @AnimRes enterAnim: Int = 0,
    @AnimatorRes @AnimRes exitAnim: Int = 0,
    @AnimatorRes @AnimRes popEnterAnim: Int = 0,
    @AnimatorRes @AnimRes popExitAnim: Int = 0,
    vararg sharedElement: View
) = childFragmentManager.inTransaction {
    if (addToBackStack) {
        addToBackStack(fragment.javaClass.name)
    }
    setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElement.forEach { view ->
            addSharedElement(view, view.transitionName)
        }
    }
    replace(containerViewId, fragment)
}

fun Fragment.removeFragment(vararg names: String) {
    childFragmentManager.inTransaction {
        names.forEach { name ->
            childFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val fragment = childFragmentManager.findFragmentByTag(name)
            if (fragment != null) {
                remove(fragment)
            }
        }
    }
}
//==================================================Fragment=========================================================//


//==================================================FragmentActivity====================================================//
@SuppressLint("ObsoleteSdkInt")
fun FragmentActivity.addFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    @AnimatorRes @AnimRes enterAnim: Int = 0,
    @AnimatorRes @AnimRes exitAnim: Int = 0,
    @AnimatorRes @AnimRes popEnterAnim: Int = 0,
    @AnimatorRes @AnimRes popExitAnim: Int = 0,
    vararg sharedElement: View
) = supportFragmentManager.inTransaction {
    if (addToBackStack) {
        addToBackStack(fragment.javaClass.name)
    }
    setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElement.forEach { view ->
            addSharedElement(view, view.transitionName)
        }
    }
    if (!fragment.isAdded) {
        add(containerViewId, fragment, fragment.javaClass.name)
    }
}

@SuppressLint("ObsoleteSdkInt")
fun FragmentActivity.hideShowFragment(
    @IdRes containerViewId: Int,
    newFragment: Fragment,
    previousFragment: Fragment? = null,
    addToBackStack: Boolean = false,
    @AnimatorRes @AnimRes enterAnim: Int = 0,
    @AnimatorRes @AnimRes exitAnim: Int = 0,
    @AnimatorRes @AnimRes popEnterAnim: Int = 0,
    @AnimatorRes @AnimRes popExitAnim: Int = 0,
    vararg sharedElement: View
) = supportFragmentManager.inTransaction {
    if (addToBackStack) {
        addToBackStack(newFragment.javaClass.name)
    }
    setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElement.forEach { view ->
            addSharedElement(view, view.transitionName)
        }
    }
    if (previousFragment != null) {
        hide(previousFragment)
    }
    if (newFragment.isAdded) {
        show(newFragment)
    } else {
        add(containerViewId, newFragment, newFragment.javaClass.name)
        show(newFragment)
    }
}

@SuppressLint("ObsoleteSdkInt")
fun FragmentActivity.replaceFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    @AnimatorRes @AnimRes enterAnim: Int = 0,
    @AnimatorRes @AnimRes exitAnim: Int = 0,
    @AnimatorRes @AnimRes popEnterAnim: Int = 0,
    @AnimatorRes @AnimRes popExitAnim: Int = 0,
    vararg sharedElement: View
) = supportFragmentManager.inTransaction {
    if (addToBackStack) {
        addToBackStack(fragment.javaClass.name)
    }
    setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElement.forEach { view ->
            addSharedElement(view, view.transitionName)
        }
    }
    replace(containerViewId, fragment)
}

fun FragmentActivity.removeFragment(vararg names: String) {
    supportFragmentManager.inTransaction {
        names.forEach { name ->
            supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val fragment = supportFragmentManager.findFragmentByTag(name)
            if (fragment != null) {
                remove(fragment)
            }
        }
    }
}
//=================================================FragmentActivity==================================================//