package com.am.zviryata.di

import com.am.shared.shared.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    fun provideGameRepository(): GameRepository {
        return GameRepository()
    }
}
