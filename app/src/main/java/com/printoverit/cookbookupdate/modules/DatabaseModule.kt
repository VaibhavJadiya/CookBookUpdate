package com.printoverit.cookbookupdate.modules

import android.content.Context
import androidx.room.Room
import com.printoverit.cookbookupdate.room.RecipiesDatabase
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context,
            RecipiesDatabase::class.java,
            DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDao(database: RecipiesDatabase)=database.recipiesDao()
}