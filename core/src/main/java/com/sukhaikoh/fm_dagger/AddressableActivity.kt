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

package com.sukhaikoh.fm_dagger

import android.app.Activity
import android.content.Intent

/**
 * An [Activity] that can be addressed by an [Intent].
 */
interface AddressableActivity {
    /**
     * The [Activity] package name.
     *
     * i.e `com.my.package`
     */
    val packageName: String

    /**
     * The [Activity] class name.
     *
     * i.e `com.my.package.MyActivity`
     */
    val className: String
}

/**
 * Create an [Intent] with the given [addressableActivity].
 *
 * ### Example
 * ```
 * object MyActivity : AddressableActivity {
 *     override val packageName = "com.my.package"
 *     override val className = "com.my.package.MyActivity"
 * }
 *
 * object MyModule : FeatureModule {
 *     override val injectorName = "com.my.package.MyInjector"
 * }
 *
 *
 * // In an Activity or Fragment
 * inject(MyModule)
 * startActivity(intentTo(MyActivity))
 *
 * // Or with the extension function
 * startActivity(MyModule, intentTo(MyActivity))
 * ```
 *
 * @param addressableActivity the [AddressableActivity] this new intent can address to.
 * @param intentAction the intent action, such as [Intent.ACTION_VIEW].
 * @return a new [Intent] that is addressable by the [addressableActivity].
 */
fun intentTo(
    addressableActivity: AddressableActivity,
    intentAction: String = Intent.ACTION_VIEW
): Intent {
    return Intent(intentAction).setClassName(
        addressableActivity.packageName,
        addressableActivity.className
    )
}