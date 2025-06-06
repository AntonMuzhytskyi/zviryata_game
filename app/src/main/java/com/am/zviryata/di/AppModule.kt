package com.am.zviryata.di

import com.am.shared.shared.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    fun provideGameRepository(): GameRepository {
        return GameRepository()
    }
}
