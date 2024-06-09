package com.pelagohealth.codingchallenge.common.testhelper

import com.pelagohealth.codingchallenge.domain.model.Fact

object FactMother {

    fun createFactList() = listOf(
        createDefaultFact(text = "name1"),
        createDefaultFact(text = "name2"),
        createDefaultFact(text = "name3"),
        createDefaultFact(text = "name4"),
    )

    fun createFactListAfterUpdate() = listOf(
        createDefaultFact(text = "name2"),
        createDefaultFact(text = "name3"),
        createDefaultFact(text = "name4"),
        createDefaultFact(text = "name5"),
    )

}

private fun createDefaultFact(
    text: String = "",
    url: String = "http://www.djtech.net/humor/useless_facts.htm"
): Fact = Fact(text, url)