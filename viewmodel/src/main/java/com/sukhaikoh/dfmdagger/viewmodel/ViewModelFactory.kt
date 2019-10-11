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

package com.sukhaikoh.dfmdagger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * A [ViewModelProvider.Factory] that stores a map of [ViewModel]s and provide
 * a necessary [ViewModel] when requested.
 *
 * The following code is an example of binding a [ViewModel]
 * into the dagger internal map, so this factory class will find the [ViewModel]
 * when requested.
 *
 * ```
 * @Module
 * abstract class MyViewModelModule : ViewModelModule {
 *     // Bind the desired ViewModel into dagger internal map
 *     @Binds
 *     @IntoMap
 *     @ViewModelKey(MyViewModel::class)
 *     abstract fun bindMyViewModel(viewModel: MyViewModel): ViewModel
 * }
 *
 * MyViewModel.kt
 * ---------------------
 * class MyViewModel @Inject constructor() : ViewModel() {
 *    ...
 * }
 * ```
 *
 * @param creators A collection of [ViewModel] stored in the dagger internal map.
 */
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}