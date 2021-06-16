package com.nikoarap.slotmachine.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.nikoarap.slotmachine.db.classes.JackpotDataBase
import com.nikoarap.slotmachine.db.dao.JackpotDao
import com.nikoarap.slotmachine.other.Constants.JACKPOT_DATABASE_NAME
import com.nikoarap.slotmachine.other.Constants.KEY_NAME
import com.nikoarap.slotmachine.other.Constants.KEY_TOGGLE_FIRST_TIME
import com.nikoarap.slotmachine.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        JackpotDataBase::class.java,
        JACKPOT_DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideJackpotDao(db: JackpotDataBase) = db.getJackpotDao()



    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideFirstTime(sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean(KEY_TOGGLE_FIRST_TIME, true)

    @Singleton
    @Provides
    @Named("userName")
    fun provideUserName(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(KEY_NAME, "") ?: ""




}
