package com.pelagohealth.codingchallenge.data.repository

import com.pelagohealth.codingchallenge.common.ApiResult
import com.pelagohealth.codingchallenge.common.apiRunCatching
import com.pelagohealth.codingchallenge.data.db.FactDataSource
import com.pelagohealth.codingchallenge.data.network.FactApi
import com.pelagohealth.codingchallenge.data.network.dto.toFact
import com.pelagohealth.codingchallenge.domain.api.FactRepository
import com.pelagohealth.codingchallenge.domain.model.Fact
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FactAccessor(
    private val factApi: FactApi,
    private val factDataSource: FactDataSource,
    private val ioContext: CoroutineDispatcher,
) : FactRepository {

    override suspend fun get(): ApiResult<Unit> {
        return apiRunCatching {
            withContext(ioContext) {
                val fact = factApi.getFact().toFact()
                factDataSource.insertFacts(listOf(fact))
            }
        }
    }

    override fun getFacts(): Flow<List<Fact>> {
        return factDataSource.getFacts().flowOn(ioContext)
    }

    override suspend fun deleteFact(text: String) {
        withContext(ioContext) {
            factDataSource.deleteFact(text)
        }
    }

    override suspend fun deleteAll() {
        withContext(ioContext) {
            factDataSource.purgeDatabase()
        }
    }


}