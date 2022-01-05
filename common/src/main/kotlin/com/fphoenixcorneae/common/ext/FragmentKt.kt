package com.fphoenixcorneae.common.ext

import android.content.Context
import android.os.Bundle
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

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> Context.newInstanceFragment(): T {
    val args = Bundle()
    val className = T::class.java.name;
    val clazz = FragmentFactory.loadFragmentClass(classLoader, className)
    val f = clazz.getConstructor().newInstance()
    args.classLoader = f.javaClass.classLoader
    f.arguments = args
    return f as T
}

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> Context.newInstanceFragment(args: Bundle?): T {
    val className = T::class.java.name;
    val clazz = FragmentFactory.loadFragmentClass(classLoader, className)
    val f = clazz.getConstructor().newInstance()
    if (args != null) {
        args.classLoader = f.javaClass.classLoader
        f.arguments = args
    }
    return f as T
}

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> Context.newInstanceFragment(vararg pair: Pair<String, String>): T {
    val args = Bundle()
    pair.let {
        for (arg in pair) {
            args.putString(arg.first, arg.second)
        }
    }
    val className = T::class.java.name;
    val clazz = FragmentFactory.loadFragmentClass(classLoader, className)
    val f = clazz.getConstructor().newInstance()
    args.classLoader = f.javaClass.classLoader
    f.arguments = args
    return f as T
}


inline fun FragmentManager.inTransaction(
    func: FragmentTransaction.() -> FragmentTransaction
) = beginTransaction().func().commit()

fun Fragment.addFragment(
    fragment: Fragment,
    frameId: Int
) = childFragmentManager.inTransaction { add(frameId, fragment) }

fun Fragment.replaceFragment(
    fragment: Fragment,
    frameId: Int
) = childFragmentManager.inTransaction { replace(frameId, fragment) }

fun FragmentActivity.addFragment(
    fragment: Fragment,
    frameId: Int
) = supportFragmentManager.inTransaction { add(frameId, fragment) }

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int
) = supportFragmentManager.inTransaction { replace(frameId, fragment) }