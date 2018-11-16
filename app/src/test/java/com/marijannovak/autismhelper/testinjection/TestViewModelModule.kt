package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.viewmodels.AACViewModel
import com.marijannovak.autismhelper.viewmodels.ChildViewModel
import com.marijannovak.autismhelper.viewmodels.LoginViewModel
import com.marijannovak.autismhelper.viewmodels.MainViewModel
import com.marijannovak.autismhelper.viewmodels.ParentViewModel
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