package test.fueled.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager private constructor() {

    val BASE_URL = "https://api.foursquare.com/v2/"
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build()

    public fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    private object HOLDER {
        val INSTANCE = ApiManager()
    }

    companion object {
        val instance: ApiManager by lazy { HOLDER.INSTANCE }
    }
}