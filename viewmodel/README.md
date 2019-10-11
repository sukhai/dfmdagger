## dfmdagger:viewmodel
A small Android library that provides a single [ViewModelProvider.Factory](https://developer.android.com/reference/android/arch/lifecycle/ViewModelProvider.Factory)
that can create different [ViewModel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html).
So you do not have to create multiple `ViewModelProvider.Factory` per `ViewModel`.

#### So, you do this
```kotlin
class ViewModel1 @Inject constructor() : ViewModel() {
    // ...
}

class ViewModel2 @Inject constructor() : ViewModel() {
    // ...
}

@Module(includes = [ViewModelModule::class])
abstract class MyViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ViewModel1::class)
    abstract fun bindViewModel1(viewModel: ViewModel1): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModel2::class)
    abstract fun bindViewModel2(viewModel: ViewModel2): ViewModel
}
```

#### Instead of
```kotlin
class ViewModel1 @Inject constructor() : ViewModel() {
    // ...

    class MyViewModelFactory1 @Inject constructor() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return super.create(modelClass)
        }
    }
}

class ViewModel2 @Inject constructor() : ViewModel() {
    // ...

    class MyViewModelFactory2 @Inject constructor() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return super.create(modelClass)
        }
    }
}

@Module
abstract class MyModule {
    @Binds
    abstract fun bindViewModelFactory1(factory: ViewModel1.MyViewModelFactory1): ViewModel1.MyViewModelFactory1

    @Binds
    abstract fun bindViewModelFactory2(factory: ViewModel2.MyViewModelFactory2): ViewModel2.MyViewModelFactory2

    @Binds
    abstract fun bindViewModel1(viewModel: ViewModel1): ViewModel
    
    @Binds
    abstract fun bindViewModel2(viewModel: ViewModel2): ViewModel
}
```

### Usage
1. Create a dagger module that includes all your `ViewModel` class bindings. After that
you can declare `@Module(includes = [ViewModelModule::class])` in your dagger module, or
`@Component(modules = [ViewModelModule::class]`
i.e
```kotlin
@Module(includes = [ViewModelModule::class])
abstract class MyViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ViewModel1::class)
    abstract fun bindViewModel1(viewModel: ViewModel1): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModel2::class)
    abstract fun bindViewModel2(viewModel: ViewModel2): ViewModel
}
```

#### Use with dfmdagger module
This library support using dagger with Android Dynamic Module. To do that, it will require
dependency to `dfmdagger` module.

Once you imported `dfmdagger` into gradle and follow the steps to setup `dfmdagger` (see README
from that library), then there are 2 things you will need to do.

**Step 1**
In your app component (i.e `AppComponent`), include `ViewModelModule`.
For example, say your app component is called `AppComponent`:
```kotlin
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,  <-- add this
        ...
    ]
)
interface AppComponent : AndroidInjector<YourApp> {
    ...
}
```

**Step 2**
In your dynamic feature module, include a dagger module that declares all your `ViewModel`s, then
set it to your dynamic feature module's component.
For example, say your feature module component is called `MyFeatureComponent`:
```kotlin
@Module
abstract class MyFeatureModule {
    @Binds
    @IntoMap
    @ViewModelKey(MyFeatureViewModel::class)
    abstract fun bindMyFeatureViewModel(viewModel: MyFeatureViewModel): ViewModel
    
    ...
}

@FeatureScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        MyFeatureModule::class
    ]
)
interface MyFeatureComponent {
    ...
}
```

### Download

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
   