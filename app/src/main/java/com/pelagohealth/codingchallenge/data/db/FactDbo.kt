package com.pelagohealth.codingchallenge.data.db

import com.pelagohealth.codingchallenge.domain.model.Fact
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class FactDbo() : RealmObject {

	constructor(
		text: String = "",
		url: String = ""
	) : this() {
		this.text = text
		this.url = url
	}

	@PrimaryKey
	var id: ObjectId = BsonObjectId()
	var added: RealmInstant = RealmInstant.fromMillis(System.currentTimeMillis())
	var text: String = ""
	var url: String = ""

}

fun Fact.toDbo(): FactDbo = FactDbo(
	text = this.text,
	url = this.url
)

fun FactDbo.toFact(): Fact = Fact(
	text = this.text,
	url = this.url
)