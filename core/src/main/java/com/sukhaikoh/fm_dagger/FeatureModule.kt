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

/**
 * An interface for declaring an object as a feature module that can be injected into dagger
 * dependency graph.
 *
 * Any feature that's in another project module must be injected into dagger dependency graph
 * before any of the class can be used, and often time the injection has to be done before
 * accessing the module.
 *
 * To achieve this, one must create a class that extends [FeatureModuleInjector] and override
 * [injectorName] with the full name of the injector class.
 */
interface FeatureModule {
    /**
     * The name of the class that extends [FeatureModuleInjector]. This name must be include
     * the package name as well.
     *
     * i.e
     * ```
     * override val injectorName = "com.my.package.MyInjector"
     * ```
     */
    val injectorName: String
}