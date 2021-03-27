package com.mohamed.medhat.sanad.utils.parsers

import org.junit.Test

/**
 * Test class for testing [StringToDateParser].
 */
class StringToDateParserTest {
    @Test
    fun test_parse_fun() {
        val future = StringToDateParser.parse("2021-05-07T13:16:14.0585213Z")
        val past = StringToDateParser.parse("2021-02-07T13:16:13.0585213Z")
        println("Date: ${future.toString()}")
        println("future > past: ${future?.compareTo(past)}")
        println("past < future: ${past?.compareTo(future)}")
        println("future = future: ${future?.compareTo(future)}")
        println("future.after(past): ${future?.after(past)}")
        println("past.after(future): ${past?.after(future)}")
    }
}