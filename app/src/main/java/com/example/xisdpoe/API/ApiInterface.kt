package com.example.xisdpoe.API

import com.example.xisdpoe.models.CustomerModel
import com.example.xisdpoe.models.PaymentIntentModel
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import com.example.xisdpoe.Utils.SECRET_KEY

interface ApiInterface {


    @Headers("Authorization: Bearer $SECRET_KEY")
    @POST("v1/customers")
    suspend fun getCustomer(): Response<CustomerModel>


    @Headers("Authorization: Bearer $SECRET_KEY", "Stripe-Version: 2024-04-10")
    @POST("v1/ephemeral_keys")
    suspend fun getEpheremalKey(
        @Query("customer") customer: String
    ): Response<CustomerModel>

    @Headers("Authorization: Bearer $SECRET_KEY", "Stripe-Version: 2024-04-10")
    @POST("payment_intents")
    suspend fun getPaymentIntent(

        @Query("customer") customer: String,
        @Query("amount") amount: String = "100",
        @Query("currency") currency: String = "inr",
        @Query("automatic_payment_methods[enabled]") automatePay: Boolean = true,

        ): Response<PaymentIntentModel>

}



