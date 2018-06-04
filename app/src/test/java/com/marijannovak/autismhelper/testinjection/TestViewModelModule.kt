package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import com.marijannovak.autismhelper.modules.child.mvvm.ChildViewModel
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
class TestViewModelModule {

    @Provides
    fun provideLoginViewModel(): LoginViewModel {
        return Mockito.mock(LoginViewModel::class.java)
    }

    @Provides
    fun provideMainViewModel(): MainViewModel {
        return Mockito.mock(MainViewModel::class.java)
    }

    @Provides
    fun provideChildViewModel(): ChildViewModel {
        return Mockito.mock(ChildViewModel::class.java)
    }

    @Provides
    fun provideParentViewModel(): ParentViewModel {
        return Mockito.mock(ParentViewModel::class.java)
    }

    @Provides
    fun provideAACViewModel(): AACViewModel {
        return Mockito.mock(AACViewModel::class.java)
    }

}