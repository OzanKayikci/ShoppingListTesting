package com.laivinieks.shoppinglisttesting.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.laivinieks.shoppinglisttesting.data.local.ShoppingDao
import com.laivinieks.shoppinglisttesting.data.local.ShoppingItemDb
import com.laivinieks.shoppinglisttesting.data.remote.PixabayAPI
import com.laivinieks.shoppinglisttesting.other.Constants.BASE_URL
import com.laivinieks.shoppinglisttesting.other.Resource
import com.laivinieks.shoppinglisttesting.repositories.DefaultShoppingRepository
import com.laivinieks.shoppinglisttesting.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDB(
        app: Application
    ): ShoppingItemDb {

        return Room.databaseBuilder(
            app,
            ShoppingItemDb::class.java,
            ShoppingItemDb.DATABASE_NAME
        ).build()
    }

    //we did it because of we ask an interface as parameter for viewModal
    // and dagger-hilt doesn't provide it. so we create and cast it in here
    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(dao: ShoppingDao, api: PixabayAPI): ShoppingRepository {
        return DefaultShoppingRepository(dao, api)
    }

    @Singleton
    @Provides
    fun provideShoppingDao(db: ShoppingItemDb): ShoppingDao {
        return db.shoppingDao()
    }

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(PixabayAPI::class.java)
    }
}