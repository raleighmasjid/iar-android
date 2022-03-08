package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.api.decodeList
import org.raleighmasjid.iar.model.json.PrayerDay

class ModelParsingTest {
    @Test
    fun parsingPrayerTimes() {

        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(testJSON)
        Assert.assertEquals(2, prayerDays?.count())
    }

    @Test
    fun parsingHijriDate() {
        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(testJSON)
        val hijri = prayerDays!![0].hijri
        Assert.assertEquals("Jumada al-thani", hijri.monthName)
        Assert.assertEquals(28, hijri.day)
        Assert.assertEquals(6, hijri.month)
        Assert.assertEquals(1443, hijri.year)
    }

    @Test
    fun adhanTimes() {
        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(testJSON)
        val adhan = prayerDays!![0].adhan

        Assert.assertEquals("Mon Jan 31 05:48:00 EST 2022", adhan.fajr.toString())
        Assert.assertEquals("Mon Jan 31 07:16:00 EST 2022", adhan.shuruq.toString())
        Assert.assertEquals("Mon Jan 31 12:33:00 EST 2022", adhan.dhuhr.toString())
        Assert.assertEquals("Mon Jan 31 15:20:00 EST 2022", adhan.asr.toString())
        Assert.assertEquals("Mon Jan 31 17:44:00 EST 2022", adhan.maghrib.toString())
        Assert.assertEquals("Mon Jan 31 19:05:00 EST 2022", adhan.isha.toString())
    }

    @Test
    fun iqamahTimes() {
        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(testJSON)
        val iqamah = prayerDays!![0].iqamah

        Assert.assertEquals("Mon Jan 31 06:15:00 EST 2022", iqamah.fajr.toString())
        Assert.assertEquals("Mon Jan 31 13:35:00 EST 2022", iqamah.dhuhr.toString())
        Assert.assertEquals("Mon Jan 31 15:30:00 EST 2022", iqamah.asr.toString())
        Assert.assertEquals("Mon Jan 31 17:54:00 EST 2022", iqamah.maghrib.toString())
        Assert.assertEquals("Mon Jan 31 19:30:00 EST 2022", iqamah.isha.toString())
        Assert.assertNull(iqamah.taraweeh)
    }

    private val testJSON =
        """
            [
              {
                "date": "2022-01-31T00:00:00-05:00",
                "hijri": {
                  "month": "Jumada al-thani",
                  "day": 28,
                  "year": 1443,
                  "month_numeric": 6
                },
                "adhan": {
                  "fajr": "2022-01-31T05:48:00-05:00",
                  "shuruq": "2022-01-31T07:16:00-05:00",
                  "dhuhr": "2022-01-31T12:33:00-05:00",
                  "asr": "2022-01-31T15:20:00-05:00",
                  "maghrib": "2022-01-31T17:44:00-05:00",
                  "isha": "2022-01-31T19:05:00-05:00"
                },
                "iqamah": {
                  "fajr": "2022-01-31T06:15:00-05:00",
                  "dhuhr": "2022-01-31T13:35:00-05:00",
                  "asr": "2022-01-31T15:30:00-05:00",
                  "maghrib": "2022-01-31T17:54:00-05:00",
                  "isha": "2022-01-31T19:30:00-05:00",
                  "taraweeh": null
                }
              },
              {
                "date": "2022-02-01T00:00:00-05:00",
                "hijri": {
                  "month": "Jumada al-thani",
                  "day": 29,
                  "year": 1443,
                  "month_numeric": 6
                },
                "adhan": {
                  "fajr": "2022-02-01T05:48:00-05:00",
                  "shuruq": "2022-02-01T07:15:00-05:00",
                  "dhuhr": "2022-02-01T12:33:00-05:00",
                  "asr": "2022-02-01T15:21:00-05:00",
                  "maghrib": "2022-02-01T17:45:00-05:00",
                  "isha": "2022-02-01T19:06:00-05:00"
                },
                "iqamah": {
                  "fajr": "2022-02-01T06:15:00-05:00",
                  "dhuhr": "2022-02-01T13:35:00-05:00",
                  "asr": "2022-02-01T16:00:00-05:00",
                  "maghrib": "2022-02-01T17:55:00-05:00",
                  "isha": "2022-02-01T19:30:00-05:00",
                  "taraweeh": null
                }
              }]
        """
}