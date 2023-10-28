package com.sparklead.evocharge.di

import com.sparklead.evocharge.service.AuthService
import com.sparklead.evocharge.service.ChargingStationService
import com.sparklead.evocharge.service.UserService
import com.sparklead.evocharge.ui.repositories.ChargingStationRepository
import com.sparklead.evocharge.ui.repositories.ChargingStationRepositoryImp
import com.sparklead.evocharge.ui.repositories.CompleteProfileRepository
import com.sparklead.evocharge.ui.repositories.CompleteProfileRepositoryImp
import com.sparklead.evocharge.ui.repositories.SignInRepository
import com.sparklead.evocharge.ui.repositories.SignInRepositoryImp
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
    fun providesSignUpRepository(service: AuthService): SignUpRepository =
        SignUpRepositoryImp(service)

    @Provides
    fun providesCompleteProfileRepository(service: UserService): CompleteProfileRepository =
        CompleteProfileRepositoryImp(service)

    @Provides
    fun providesSignInRepository(service: AuthService): SignInRepository =
        SignInRepositoryImp(service)

    @Provides
    fun providesChargingStationRepository(service: ChargingStationService): ChargingStationRepository =
        ChargingStationRepositoryImp(service)
}