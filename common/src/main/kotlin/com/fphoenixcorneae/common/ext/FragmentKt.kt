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

inline fun <reified T : Fragment> Fragment.newInstanceFragment(): T {
    return requireContext().newInstanceFragment()
}

inline fun <reified T : Fragment> Fragment.newInstanceFragment(args: Bundle?): T {
    return requireContext().newInstanceFragment(args)
}

inline fun <reified T : Fragment> Fragment.newInstanceFragment(vararg pair: Pair<String, String>): T {
    return requireContext().newInstanceFragment(*pair)
}

/**实例化 Fragment*/
inline fun <reified T : Fragment> Context.newInstanceFragment(args: Bundle? = null): T {
    val className = T::class.java.name
    val clazz = FragmentFactory.loadFragmentClass(classLoader, className)
    val f = clazz.getConstructor().newInstance()
    (args ?: Bundle()).also {
        it.classLoader = f.javaClass.classLoader
        f.arguments = it
    }
    return f as T
}

/**实例化 Fragment*/
inline fun <reified T : Fragment> Context.newInstanceFragment(vararg pair: Pair<String, String>): T {
    val className = T::class.java.name
    val clazz = FragmentFactory.loadFragmentClass(classLoader, className)
    val f = clazz.getConstructor().newInstance()
    val args = Bundle()
    pair.forEach { arg ->
        args.putString(arg.first, arg.second)
    }
    args.classLoader = f.javaClass.classLoader
    f.arguments = args
    return f as T
}


inline fun FragmentManager.inTransaction(
    func: FragmentTransaction.() -> FragmentTransaction
) = run {
    beginTransaction().func().commitAllowingStateLoss()
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
    apply {
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
    apply {
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
    apply {
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
}

fun Fragment.removeFragment(vararg names: String) {
    childFragmentManager.inTransaction {
        apply {
            names.forEach { name ->
                childFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val fragment = childFragmentManager.findFragmentByTag(name)
                if (fragment != null) {
                    remove(fragment)
                }
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
    apply {
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
    apply {
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
    apply {
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
}

fun FragmentActivity.removeFragment(vararg names: String) {
    supportFragmentManager.inTransaction {
        apply {
            names.forEach { name ->
                supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val fragment = supportFragmentManager.findFragmentByTag(name)
                if (fragment != null) {
                    remove(fragment)
                }
            }
        }
    }
}
//=================================================FragmentActivity==================================================//