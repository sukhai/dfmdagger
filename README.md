[![Build Status](https://travis-ci.com/sukhai/fm_dagger.svg?branch=master)](https://travis-ci.com/sukhai/fm_dagger)

# fm_dagger
A small Android library that reduce the boilerplate code for your project when using
Android Dagger. This library allows you to use the Android Dagger (i.e `@ContributesAndroidInjector`) 
to resolve the dependencies in Dynamic Feature Module without the need of writing boilerplate code. 

### Background
This project is inspired by [Dependency injection in a multi module project](https://medium.com/androiddevelopers/dependency-injection-in-a-multi-module-project-1a09511c14b7)
where the article shows how we can use Dagger to inject modules' components into the app component.
While that works well for an Android project with Dynamic Feature Module, but I think it can be
done with Android Dagger as well with some little tweaks.

The main difference here is that we use reflection to get the injector of a feature module and
inject the feature module component into the application component before launching or accessing
any classes in the feature module.

### Usage
#### In `app` module
```kotlin
// MyApp.kt
class MyApp : FeatureModuleApplication() {
    override fun applicationInjector(): AndroidInjector<out FeatureModuleApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}

// Create an extension function exposing appComponent() from this application class
fun Application.appComponent() = ((this as MyApp).component) as AppComponent

// AppModule.kt
@Module
class AppModule {
    // ...
}

// AppComponent.kt
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        
        fun build(): AppComponent
    }
}

// FeatureOneModule.kt
// Creating this class just to hold all the hard coded package/class names into 1 file
object FeatureOneModule : FeatureModule {
    override val injectorName = "com.my.package.featureone.FeatureOneInjector"
    
    // if you want to access the feature module's activity, implement AddressableActivity
    object FeatureOneActivity : AddressableActivity {
        override val packageName = "com.my.package"
        override val className = "com.my.package.featureone.FeatureOneActivity"
    }
    
    // if you want to access the feature module's fragment, implement AddressableObject
    object FeatureOneFragment : AddressableObject {
        override val className = "com.my.package.featureone.FeatureOneFragment"
    }
}

// MainActivity.kt
// Demonstrating how to launch other feature module
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // demo of launching another feature module activity
        startActivity(FeatureOneModule, intentTo(FeatureOneModule.FeatureOneActivity))
        
        // demo of launching another feature module fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.my_container, newFragment(FeatureOneModule.FeatureOneFragment))
            .commitNow(FeatureOneModule)
    }
}
```

#### In your feature module
```kotlin
// FeatureOneModule.kt
package com.my.package.featureone

@Module
class FeatureOneModule {
    // ...
}

// FeatureOneActivityModule.kt
@Module
abstract class FeatureOneActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeFeatureOneActivity(): FeatureOneActivity
    
    @ContributesAndroidInjector
    abstract fun contributeFeatureOneFragment(): FeatureOneFragment
}

// FeatureOneComponent.kt
@FeatureScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        AndroidInjectionModule::class,
        FeatureOneActivityModule::class
    ]
)
interface FeatureOneComponent {
    fun inject(injector: FeatureOneInjector)
}

// FeatureOneInjector
// make sure this package name and class name are the same as you hard coded in the app module
package com.my.package.featureone

@Keep // adding this annotation so proguard/r8 will keep it for reflection purpose
class FeatureOneInjector : FeatureModuleInjector() {
    override fun inject(application: FeatureModuleApplication) {
        DaggerFeatureOneComponent.builder()
            .appComponent(application.appComponent())
            .build()
            .inject(this)
    }
}

// FeatureOneActivity.kt
package com.my.package.featureone

class FeatureOneActivity : DaggerActivity() {
    // ...
    @Inject lateinit var someDependency: SomeDependency
}

// FeatureOneFragment.kt
package com.my.package.featureone

class FeatureOneFragment : DaggerFragment() {
    // ...
    @Inject lateinit var someDependency: SomeDependency
}
```

### Important Notes
* If your feature module class name has changed, you must update all the hard
coded class name in your `app` module as well.
* The feature module will not be able to support `@Singleton` annotation. To
have singleton in your feature module, you must make sure the class is singleton
by default. You can achieve it using kotlin `object MyClass` or create the singleton
pattern yourself.
* The extension functions provided by this library 
(i,e `startActivity(FeatureModule, Intent, Bundle?)`) should satisfy most of
the use cases where launching activity/fragment from other feature module.
However, if you want to launch your feature module only to access its classes,
you can inject the feature module injector first then use reflection to obtain
the desired feature module class. For example:
```kotlin
// ServiceA.kt, must reside in `app` module, or a module that `app` has access to.
interface ServiceA {
    fun doSomething()
}

// In your feature module
// FeatureOneService.kt
package com.my.package.featureone

class FeatureOneService : ServiceA {
    override fun doSomething() {
        // ...
    }
}

// In your app module or other module that wants to access implementation of ServiceA
object FeatureOneServiceObject : AddressableObject {
    override val className = "com.my.package.featureone.FeatureOneService"
}

// Somewhere in your code where you want to access the implementation of ServiceA
// Note we specify the type as the interface name ServiceA instead of the implementation class
// FeatureOneService, this is because we might not have access to the FeatureOne module.
val serviceA = FeatureOneServiceObject.newInstance<ServiceA>()

// or
FeatureOneServiceObject.newInstance<ServiceA>() { serviceA -> serviceA.doSomething() }
```

# Download
Coming soon.

# License
```
Copyright (C) 2019 Su Khai Koh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
   