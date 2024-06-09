package com.pelagohealth.codingchallenge.data.db

import com.pelagohealth.codingchallenge.domain.model.Fact
import kotlinx.coroutines.flow.Flow

interface FactDataSource {

    suspend fun purgeDatabase()

    suspend fun insertFacts(facts: List<Fact>)

    suspend fun deleteFact(text: String)

    fun getFacts(): Flow<List<Fact>?>

    fun getFact(id: Int): Fact

}
