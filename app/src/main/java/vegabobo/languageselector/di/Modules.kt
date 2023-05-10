package vegabobo.languageselector.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vegabobo.languageselector.LocaleManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Modules {

    @Singleton
    @Provides
    fun provideLocaleManager(): LocaleManager {
        return LocaleManager()
    }
}