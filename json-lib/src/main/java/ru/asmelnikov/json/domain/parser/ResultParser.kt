package ru.asmelnikov.json.domain.parser

import java.lang.reflect.Type

interface ResultParser {

    fun <T> parse(type: Type, data: Any): T

}