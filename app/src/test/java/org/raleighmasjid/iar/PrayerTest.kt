package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.model.Prayer

class PrayerTest {

    @Test
    fun isFajrEnumWorking() {

        val fajr = Prayer.FAJR
        Assert.assertEquals("Fajr", fajr.title())
        Assert.assertEquals(0L, fajr.notificationOffset())
    }

    @Test
    fun isShuruqEnumWorking() {

        val shuruq = Prayer.SHURUQ
        Assert.assertEquals("Shuruq", shuruq.title())
        Assert.assertEquals(30L, shuruq.notificationOffset())
    }

    @Test
    fun isDhuhrEnumWorking() {

        val dhuhr = Prayer.DHUHR
        Assert.assertEquals("Dhuhr", dhuhr.title())
        Assert.assertEquals(0L, dhuhr.notificationOffset())
    }

    @Test
    fun isAsrEnumWorking() {

        val asr = Prayer.ASR
        Assert.assertEquals("Asr", asr.title())
        Assert.assertEquals(0L, asr.notificationOffset())
    }

    @Test
    fun isMaghribEnumWorking() {

        val maghrib = Prayer.MAGHRIB
        Assert.assertEquals("Maghrib", maghrib.title())
        Assert.assertEquals(0L, maghrib.notificationOffset())
    }

    @Test
    fun isIshaEnumWorking() {

        val isha = Prayer.ISHA
        Assert.assertEquals("Isha", isha.title())
        Assert.assertEquals(0L, isha.notificationOffset())
    }
}