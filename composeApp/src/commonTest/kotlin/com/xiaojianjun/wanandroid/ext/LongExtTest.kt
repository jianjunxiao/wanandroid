package com.xiaojianjun.wanandroid.ext

import kotlin.test.Test
import kotlin.test.assertTrue

class LongExtTest {
    @Test
    fun endOfDecemberUsesCalendarYear() {
        assertTrue(1577707200000L.toDateTime().startsWith("2019-12-"))
    }

    @Test
    fun startOfJanuaryUsesCalendarYear() {
        assertTrue(1609588800000L.toDateTime().startsWith("2021-01-"))
    }
}
