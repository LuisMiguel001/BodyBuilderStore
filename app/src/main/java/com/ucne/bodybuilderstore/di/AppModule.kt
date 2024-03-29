package com.ucne.bodybuilderstore.di

import android.content.Context
import androidx.room.Room
import com.ucne.bodybuilderstore.data.local.database.StoreDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesPrioridadDatabase(@ApplicationContext appContext: Context): StoreDataBase =
        Room.databaseBuilder(appContext,StoreDataBase::class.java, "Store.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePrioridadDao(db: StoreDataBase) = db.storedao()

    @Provides
    fun provideCartDao(db: StoreDataBase) = db.cartDao()
}
