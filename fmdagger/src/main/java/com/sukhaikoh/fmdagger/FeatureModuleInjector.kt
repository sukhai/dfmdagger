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

package com.sukhaikoh.fmdagger

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

/**
 * The base class for injecting a [FeatureModule].
 *
 * The implementation class must use [FeatureModuleInjector.inject] to inject a dagger component
 * into the dagger dependency graph.
 *
 * ### Example
 * ```
 * @FeatureScope
 * @Component(
 *     dependencies = [AppComponent::class],
 *     modules = [
 *         AndroidInjectionModule::class,
 *         MyFeatureActivityModule::class
 *     ]
 * )
 * interface MyFeatureComponent {
 *     fun inject(injector: MyFeatureInjector)
 * }
 *
 * @Module(includes = [ActivityModule::class])
 * class MyFeatureActivityModule {
 *     @Provide
 *     fun provideMyService(): MyService { return ... }
 *
 *     @Module
 *     abstract class ActivityModule {
 *         @ContributesAndroidInjector(modules = [FragmentModule::class])
 *         abstract fun contributeMyActivity(): MyActivity
 *     }
 *
 *     @Module
 *     abstract class FragmentModule {
 *         @ContributesAndroidInjector
 *         abstract fun contributeMyFragment(): MyFragment
 *     }
 * }
 *
 * // @Keep is required if using proguard/R8
 * @Keep
 * class MyFeatureInjector : FeatureModuleInjector() {
 *     override fun inject(application: FeatureModuleApplication) {
 *         DaggerMyFeatureComponent.builder()
 *             .appComponent(application.appComponent())
 *             .myFeatureActivityModule(MyFeatureActivityModule())
 *             .build()
 *             .inject(this)
 *     }
 * }
 *
 * // Extension function for exposing appComponent() from Application
 * fun Application.appComponent() = ((this as MyApplication).component) as AppComponent
 * ```
 */
abstract class FeatureModuleInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingBroadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>
    @Inject
    lateinit var dispatchingContentProviderInjector: DispatchingAndroidInjector<ContentProvider>
    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    /**
     * Get the [Activity] injector.
     *
     * @return [Activity] injector.
     */
    fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    /**
     * Get the [BroadcastReceiver] injector.
     *
     * @return [BroadcastReceiver] injector.
     */
    fun broadcastReceiverInjector(): DispatchingAndroidInjector<BroadcastReceiver> {
        return dispatchingBroadcastReceiverInjector
    }

    /**
     * Get the [ContentProvider] injector.
     *
     * @return [ContentProvider] injector.
     */
    fun contentProviderInjector(): DispatchingAndroidInjector<ContentProvider> {
        return dispatchingContentProviderInjector
    }

    /**
     * Get the [Fragment] injector.
     *
     * @return [Fragment] injector.
     */
    fun fragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingFragmentInjector
    }

    /**
     * Get the [Service] injector.
     *
     * @return [Service] injector.
     */
    fun serviceInjector(): DispatchingAndroidInjector<Service> {
        return dispatchingServiceInjector
    }

    /**
     * Inject this module with this injector.
     *
     * ### Example
     * ```
     * DaggerMyFeatureComponent.builder()
     *     .appComponent(application.appComponent())
     *     .myFeatureActivityModule(MyFeatureActivityModule())
     *     .build()
     *     .inject(this)
     * ```
     *
     * @param application the application.
     */
    abstract fun inject(application: FeatureModuleApplication)
}