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
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sukhaikoh.fm_dagger.FeatureModule
import com.sukhaikoh.fm_dagger.FeatureModuleApplication

/**
 * Inject the given [featureModule] if possible. It is only possible to inject the
 * [featureModule] if this [Application] is a subtype of [FeatureModuleApplication].
 *
 * If this [Application] is a subtype of [FeatureModuleApplication], then
 * [FeatureModuleApplication.inject] will be called for the given [featureModule],
 * otherwise nothing will happen.
 *
 * @param featureModule the [FeatureModule] to be injected into this [Application].
 */
fun Application.injectIfPossible(featureModule: FeatureModule) {
    if (this is FeatureModuleApplication) {
        inject(featureModule)
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
 */
fun FragmentTransaction.commitNow(inject: () -> Unit) {
    inject()

    commitNow()
}