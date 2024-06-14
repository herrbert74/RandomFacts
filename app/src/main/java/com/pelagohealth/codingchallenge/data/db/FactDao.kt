package com.pelagohealth.codingchallenge.data.db

import com.pelagohealth.codingchallenge.common.apiRunCatching
import com.pelagohealth.codingchallenge.domain.model.Fact
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FactDao @Inject constructor(private val realm: Realm) : FactDataSource {

    override suspend fun purgeDatabase() {
        realm.write {
            val facts = this.query(FactDbo::class).find()
            delete(facts)
        }
    }

    override suspend fun insertFacts(facts: List<Fact>) {
        apiRunCatching {
            realm.write {
                val currentCount = this.query<FactDbo>().count().find().toInt()
                if (currentCount > 2) {
                    val currentFacts =
                        this.query<FactDbo>()
                            .sort("added", Sort.ASCENDING)
                            .limit(currentCount - 3)
                            .find()
                    delete(currentFacts)
                }
                facts.map { copyToRealm(it.toDbo(), UpdatePolicy.ALL) }
            }
        }
    }

    override fun getFacts(): Flow<List<Fact>> {
        return realm.query(FactDbo::class).asFlow()
            .map { dbo ->
                dbo.list.map { it.toFact() }.takeIf { it.isNotEmpty() }.orEmpty()
            }
    }

    override suspend fun deleteFact(text: String) {
        realm.write {
            val factToDelete = this.query<FactDbo>("text = $0", text).find()
            delete(factToDelete)
        }
    }

    override fun getFact(id: Int): Fact {
        return realm.query<FactDbo>("id = $0", id).first().find()?.toFact() ?: Fact()
    }

}
