package vegabobo.languageselector.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vegabobo.languageselector.BuildConfig
import vegabobo.languageselector.LocaleManager
import vegabobo.languageselector.dao.AppInfoDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Modules {

    @Singleton
    @Provides
    fun provideLocaleManager(): LocaleManager {
        return LocaleManager()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAppInfoDb(app: Application): AppInfoDb {
        return Room.databaseBuilder(app, AppInfoDb::class.java, "app-info-db").build()
    }
}