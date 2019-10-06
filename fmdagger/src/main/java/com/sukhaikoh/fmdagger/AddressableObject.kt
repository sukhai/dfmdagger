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

import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance

/**
 * A object that can be addressed.
 */
interface AddressableObject {
    /**
     * The object class name.
     *
     * i.e `com.my.package.MyObject`
     */
    val className: String
}

/**
 * Creates a new instance of the class represented by [AddressableObject.className],
 * calling a constructor which either has no parameters or all parameters of which are
 * optional (see [KParameter.isOptional]). If there are no or many such constructors,
 * an exception is thrown.
 *
 * Note: this method uses [AddressableObject.className] to locate the actual class,
 * create a new instance of it, then cast it to the type [T]. It creates the new instance
 * by using either no parameters constructor or a constructor that has all parameters of
 * which are optional.
 *
 * @param T the type of the returning object. This is the type of [AddressableObject.className]
 * represents.
 * @return [T], a newly allocated instance of the class represented by
 * [AddressableObject.className].
 *
 * @throws IllegalArgumentException if the class does not have a single no-arg constructor or
 * all parameters of which are optional.
 * */
inline fun <reified T : Any> AddressableObject.newInstance(): T {
    try {
        return Class.forName(className).kotlin.createInstance() as T
    } catch (t: Throwable) {
        throw t
    }
}

/**
 * Creates a new instance of the class represented by [AddressableObject.className] then
 * invoke [block] with such object [T], calling a constructor which either has no parameters
 * or all parameters of which are optional (see [KParameter.isOptional]). If there are no or
 * many such constructors, [block] will not be invoked.
 *
 * Note: this method uses [AddressableObject.className] to locate the actual class,
 * create a new instance of it, cast it to the type [T], then invoke [block] with
 * the new instance. It creates the new instance by using either no parameters constructor
 * or a constructor that has all parameters of which are optional.
 *
 * @param T the type of the object [AddressableObject.className]. This is the type of
 * [AddressableObject.className] represents.
 * @param block the block that gets invoked if such new instance [T] is successfully created.
 */
inline fun <reified T : Any> AddressableObject.safeNewInstance(block: (T) -> Unit) {
    try {
        val t = newInstance<T>()
        block(t)
    } catch (t: Throwable) {
        // ignore
    }
}