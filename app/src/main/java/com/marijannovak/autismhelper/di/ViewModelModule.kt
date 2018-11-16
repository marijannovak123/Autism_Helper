package com.marijannovak.autismhelper.di

import androidx.lifecycle.ViewModel
import com.marijannovak.autismhelper.viewmodels.AACViewModel
import com.marijannovak.autismhelper.viewmodels.ChildViewModel
import com.marijannovak.autismhelper.viewmodels.LoginViewModel
import com.marijannovak.autismhelper.viewmodels.MainViewModel
import com.marijannovak.autismhelper.viewmodels.ParentViewModel
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