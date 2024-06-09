package com.pelagohealth.codingchallenge.data.network

import com.pelagohealth.codingchallenge.data.network.dto.FactDto
import retrofit2.http.GET

interface FactApi {

    @GET("facts/random")
    suspend fun getFact(): FactDto

}
