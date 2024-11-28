package com.nevertouchgrass.smartpertzalkaapplication

import android.content.Context
import androidx.room.Room
import com.nevertouchgrass.smartpertzalkaapplication.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideGreetingMessage(): String = "Hello"

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(applicationContext: Context): AppDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database").build()
}