package com.pelagohealth.codingchallenge.domain.api

import com.pelagohealth.codingchallenge.common.ApiResult
import com.pelagohealth.codingchallenge.domain.model.Fact
import kotlinx.coroutines.flow.Flow

interface FactRepository {

    suspend fun get():  ApiResult<Unit>

    suspend fun getFacts(): Flow<List<Fact>?>

    suspend fun deleteFact(text:String)

    suspend fun deleteAll()

}