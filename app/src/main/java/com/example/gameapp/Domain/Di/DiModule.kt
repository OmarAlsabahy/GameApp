package com.example.gameapp.Domain.Di

import android.content.Context
import androidx.room.Room
import com.example.gameapp.Data.Api.ApiHelper
import com.example.gameapp.Data.Api.AppInterceptor
import com.example.gameapp.Data.Repositories.HomeRepo
import com.example.gameapp.Data.Database.AppDatabase
import com.example.gameapp.Data.Repositories.GameDetailsRepo
import com.example.gameapp.Data.Api.EndPoints
import com.example.gameapp.Domain.Network.NetworkConnectivity
import com.example.gameapp.Domain.Repositories.IGameDetailsRepo
import com.example.gameapp.Domain.Repositories.IHomeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {
    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(AppInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(EndPoints.baseUrl)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun getApiHelper(retrofit: Retrofit): ApiHelper {
        return retrofit.create(ApiHelper::class.java)
    }

    @Provides
    @Singleton
    fun providesHomeRep(api: ApiHelper,db: AppDatabase,@ApplicationContext context: Context,
                        connectivity: NetworkConnectivity): IHomeRepo {
        return HomeRepo(api,db,context,connectivity)
    }
    @Provides
    @Singleton
    fun providesNetworkConnectivity(): NetworkConnectivity{
        return NetworkConnectivity()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase{
        val db = Room.databaseBuilder(context, AppDatabase::class.java,
            "app_database").build()
        return db
    }

    @Provides
    @Singleton
    fun providesGameDetailsRepo(api: ApiHelper): IGameDetailsRepo {
        return GameDetailsRepo(api)
    }
}