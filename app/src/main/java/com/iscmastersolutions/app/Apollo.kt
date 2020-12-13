package com.iscmastersolutions.app

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class Apollo(context: Context) {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(context))
            .build()
    }

    val client : ApolloClient = ApolloClient.builder()
        .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com")
        .subscriptionTransportFactory(
            WebSocketSubscriptionTransport.Factory(
                "wss://apollo-fullstack-tutorial.herokuapp.com/graphql",
                okHttpClient
            )
        )
        .okHttpClient(
            okHttpClient
        ).build()

    private class AuthorizationInterceptor(val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", User.getToken(context) ?: "")
                .build()

            return chain.proceed(request)
        }
    }
}