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

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * A [Fragment] that can be addressed.
 */
interface AddressableFragment {
    /**
     * The [Fragment] class name.
     *
     * i.e `com.my.package.MyFragment`
     */
    val className: String
}

/**
 * Create a new [Fragment] with the given [addressableFragment].
 *
 * ### Example
 * ```
 * object MyFragment : AddressableFragment {
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
 * @param addressableFragment the [AddressableFragment] this new [Fragment] can address to.
 * @return a new [Fragment] that is addressable by the [addressableFragment].
 */
fun newFragment(addressableFragment: AddressableFragment, args: Bundle? = null): Fragment {
    val f = Class.forName(addressableFragment.className).newInstance() as Fragment
    f.arguments = args
    return f
}