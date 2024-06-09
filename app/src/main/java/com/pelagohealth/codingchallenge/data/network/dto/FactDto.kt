package com.pelagohealth.codingchallenge.data.network.dto

import com.pelagohealth.codingchallenge.domain.model.Fact
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FactDto(
    val id: String,
    val text: String,
    @Json(name = "source_url")
    val sourceUrl: String
)

fun FactDto.toFact() = Fact(
    this.text,
    this.sourceUrl
)
