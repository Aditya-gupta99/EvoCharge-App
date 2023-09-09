package com.sparklead.evocharge.di

import com.sparklead.evocharge.service.AuthService
import com.sparklead.evocharge.ui.repositories.SignUpRepository
import com.sparklead.evocharge.ui.repositories.SignUpRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun providesSignUpRepository(service : AuthService) : SignUpRepository = SignUpRepositoryImp(service)

}