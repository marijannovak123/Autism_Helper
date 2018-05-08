package com.marijannovak.autismhelper.di

import android.arch.lifecycle.ViewModel
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import com.marijannovak.autismhelper.modules.child.mvvm.ChildViewModel
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(gameViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChildViewModel::class)
    internal abstract fun bindChildViewModel(childViewModel: ChildViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ParentViewModel::class)
    internal abstract fun bindParentViewModel(parentViewModel: ParentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AACViewModel::class)
    internal abstract fun bindAacViewModel(aacViewModel: AACViewModel): ViewModel

}