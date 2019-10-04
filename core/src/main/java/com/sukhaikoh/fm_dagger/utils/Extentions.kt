/*
 * Copyright (C) 2019 Su Khai Koh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sukhaikoh.fm_dagger.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sukhaikoh.fm_dagger.AddressableObject
import com.sukhaikoh.fm_dagger.FeatureModule
import com.sukhaikoh.fm_dagger.FeatureModuleApplication
import com.sukhaikoh.fm_dagger.newInstance

/**
 * Inject the given [featureModule] if possible. It is only possible to inject the
 * [featureModule] if this [Application] is a subtype of [FeatureModuleApplication].
 *
 * If this [Application] is a subtype of [FeatureModuleApplication], then
 * [FeatureModuleApplication.inject] will be called for the given [featureModule],
 * otherwise [onError] will be called.
 *
 * @param featureModule the [FeatureModule] to be injected into this [Application].
 */
fun Application.injectIfPossible(featureModule: FeatureModule, onError: (Throwable) -> Unit = {}) {
    if (this is FeatureModuleApplication) {
        inject(featureModule)
    } else {
        onError(IllegalStateException("This Application is not a subtype of FeatureModuleApplication"))
    }
}

/**
 * Inject the given [featureModule] if possible. It is only possible to inject the
 * [featureModule] if this [Context] is a type of [FeatureModuleApplication] or [Activity].
 *
 * If this [Context] is a type of [FeatureModuleApplication] or [Activity], then
 * [FeatureModuleApplication.inject] will be called for the given [featureModule],
 * otherwise [onError] will be called.
 *
 * @param featureModule the [FeatureModule] to be injected into this [Context].
 */
fun Context.injectIfPossible(featureModule: FeatureModule, onError: (Throwable) -> Unit = {}) {
    when (this) {
        is FeatureModuleApplication -> inject(featureModule)
        is Activity -> inject(featureModule)
        else -> onError(IllegalStateException("This Context is not a FeatureModuleApplication or Activity"))
    }
}

/**
 * Inject the given [featureModule].
 *
 * @param featureModule the [FeatureModule] to be injected into the dagger dependency graph.
 */
fun Activity.inject(featureModule: FeatureModule) {
    application.injectIfPossible(featureModule)
}

/**
 * Inject the given [featureModule].
 *
 * @param featureModule the [FeatureModule] to be injected into the dagger dependency graph.
 */
fun Fragment.inject(featureModule: FeatureModule) {
    activity?.inject(featureModule)
}

/**
 * Launch a new activity by using [Activity.startActivity], but before launching this
 * new activity, perform application injection for the given [featureModule].
 *
 * This method is basically a short hand for:
 * ```
 * application.injectIfPossible(featureModule)
 * startActivity(intent, options)
 * ```
 *
 * ### Example
 * ```
 * val packageName = "com.my.package.name"
 *
 * val myFeature = object : FeatureModule {
 *     override val injectorName = "$packageName.to.MyFeatureInjector"
 * }
 *
 * val myActivity = object : AddressableActivity {
 *     override val packageName = packageName
 *     override val className = "$packageName.to.MyActivity"
 * }
 *
 * startActivity(myFeature, intentTo(myActivity))
 * ```
 *
 * @param featureModule the [FeatureModule] to be injected into the dagger dependency graph.
 * @param intent the intent to start.
 * @param options Additional options for how the Activity should be started. See
 * [Context.startActivity] for more details.
 */
fun Activity.startActivity(
    featureModule: FeatureModule,
    intent: Intent,
    options: Bundle? = null
) {
    inject(featureModule)

    startActivity(intent, options)
}

/**
 * Launch a new activity for which you would like a result when it finished, but before launching
 * this new activity, perform application injection for the given [featureModule].
 *
 * This method is basically a short hand for:
 * ```
 * application.injectIfPossible(featureModule)
 * startActivityForResult(intent, requestCode, options)
 * ```
 *
 * ### Example
 * ```
 * val packageName = "com.my.package.name"
 *
 * val myFeature = object : FeatureModule {
 *     override val injectorName = "$packageName.to.MyFeatureInjector"
 * }
 *
 * val myActivity = object : AddressableActivity {
 *     override val packageName = packageName
 *     override val className = "$packageName.to.MyActivity"
 * }
 *
 * startActivityForResult(myFeature, intentTo(myActivity), MY_REQUEST_CODE)
 * ```
 *
 * @param featureModule the [FeatureModule] to be injected into the dagger dependency graph.
 * @param intent the intent to start.
 * @param requestCode if >= 0, this code will be returned in onActivityResult() when the activity
 * exits.
 * @param options Additional options for how the Activity should be started. See
 * [Context.startActivity] for more details.
 */
fun Activity.startActivityForResult(
    featureModule: FeatureModule,
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
) {
    inject(featureModule)

    startActivityForResult(intent, requestCode, options)
}

/**
 * Launch a new activity by using [Fragment.startActivity], but before launching this
 * new activity, perform application injection for the given [featureModule].
 *
 * @param featureModule the [FeatureModule] to be injected into the dagger dependency graph.
 * @param intent the intent to start.
 * @param options Additional options for how the Activity should be started. See
 * [Context.startActivity] for more details.
 */
fun Fragment.startActivity(
    featureModule: FeatureModule,
    intent: Intent,
    options: Bundle? = null
) {
    inject(featureModule)

    startActivity(intent, options)
}

/**
 * Launch a new activity for which you would like a result when it finished, but before launching
 * this new activity, perform application injection for the given [featureModule].
 *
 * @param featureModule the [FeatureModule] to be injected into the dagger dependency graph.
 * @param intent the intent to start.
 * @param requestCode if >= 0, this code will be returned in onActivityResult() when the activity
 * exits.
 * @param options Additional options for how the Activity should be started. See
 * [Context.startActivity] for more details.
 */
fun Fragment.startActivityForResult(
    featureModule: FeatureModule,
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
) {
    inject(featureModule)

    startActivityForResult(intent, requestCode, options)
}

/**
 * Commits this transaction synchronously. This is essentially calling
 * [FragmentTransaction.commitNow] after doing an injection for a [FeatureModule]. This is required
 * before loading any [Fragment] from other module.
 *
 * ### Example
 * ```
 * object MyModule : FeatureModule {
 *     override val injectorName = "com.my.package.MyInjector"
 * }
 *
 * // In an activity or fragment
 * supportFragmentTransaction.beginTransaction()
 *     ...
 *     .commitNow() {
 *         inject(MyModule)
 *     }
 * ```
 *
 * @param inject the block to inject a [FeatureModule] into the application.
 */
fun FragmentTransaction.commitNow(inject: () -> Unit) {
    inject()

    commitNow()
}

/**
 * Create a new [Fragment] with the given [addressableObject].
 *
 * ### Example
 * ```
 * object MyFragment : AddressableObject {
 *     override val className = "com.my.package.MyFragment"
 * }
 *
 * object MyModule : FeatureModule {
 *     override val injectorName = "com.my.package.MyInjector"
 * }
 *
 *
 * // In an Activity or Fragment
 * inject(MyModule)
 * supportFragmentManager.beginTransaction()
 *     .replace(R.id.my_container, newFragment(MyFragment))
 *     .commitNow()
 *
 * // Or with the extension function
 * supportFragmentManager.beginTransaction()
 *     .replace(R.id.my_container, newFragment(MyFragment))
 *     .commitNow(MyModule)
 * ```
 *
 * @param addressableObject the [AddressableObject] this new [Fragment] can address to.
 * @return a new [Fragment] that is addressable by the [addressableObject].
 */
fun newFragment(addressableObject: AddressableObject, args: Bundle? = null): Fragment {
    val f = addressableObject.newInstance<Fragment>()
    f.arguments = args
    return f
}