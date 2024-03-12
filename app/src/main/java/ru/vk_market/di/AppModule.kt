package ru.vk_market.di

import ru.vk_market.services.ApiFactory
import ru.vk_market.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMyRepository(repository: ApiFactory): ItemsRepository {
        return repository
    }
}