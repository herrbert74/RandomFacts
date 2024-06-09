package com.pelagohealth.codingchallenge.common.testhelper

import com.pelagohealth.codingchallenge.data.network.dto.FactDto

object FactDtoMother {

    fun createDefaultFactDto(
        text: String = "",
        url: String = "http://www.djtech.net/humor/useless_facts.htm"
    ): FactDto = FactDto("", text, url)

}
