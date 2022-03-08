package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.api.decodeList
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.model.PrayerTime
import java.text.SimpleDateFormat

class PrayerDayTest {

    @Test
    fun upcomingPrayerAsr() {

        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(testJSON)

        // Adhan time for 'asr for February 1st, 2022 is 2:45 PM
        val testAdhanDateTimeString = "2022-02-01T15:21:00-05:00"
        val testIqamahDateTimeString = "2022-02-01T16:00:00-05:00"
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val testAdhanTime = formatter.parse(testAdhanDateTimeString) // This should say:  Feb 01 15:21:00 EST 2022
        val testIqamahTime = formatter.parse(testIqamahDateTimeString)
        val prayer = Prayer.ASR
        val testPrayerTime = PrayerTime(prayer, testAdhanTime!!, testIqamahTime) // the prayer and
        // adhan from the upcoming prayer method should match this

        // Creating a fake 'current' time to pass in for it to see what the upcoming prayer time is
        val currentDateTimeString = "2022-02-01T14:45:00-05:00"
        val testCurrentTime = formatter.parse(currentDateTimeString)

        val upcomingPrayerTime = PrayerDay.upcomingPrayer(prayerDays!!, testCurrentTime!!)

        if (upcomingPrayerTime != null) {
            Assert.assertEquals(testPrayerTime.prayer, upcomingPrayerTime.prayer)
            Assert.assertEquals(testPrayerTime.adhan, upcomingPrayerTime.adhan)
            Assert.assertEquals(testPrayerTime.iqamah, upcomingPrayerTime.iqamah)
            Assert.assertEquals(testPrayerTime.notificationTime(), upcomingPrayerTime.notificationTime())
        }
    }

    @Test
    fun upcomingPrayerFajr() {

        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(testJSON)

        // Adhan time for 'asr for February 1st, 2022 is 2:45 PM
        val testAdhanDateTimeString = "2022-02-01T05:48:00-05:00"
        val testIqamahDateTimeString = "2022-02-01T06:15:00-05:00"
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val testAdhanTime = formatter.parse(testAdhanDateTimeString) // This should say:  Feb 01 15:21:00 EST 2022
        val testIqamahTime = formatter.parse(testIqamahDateTimeString)
        val prayer = Prayer.FAJR
        val testPrayerTime = PrayerTime(prayer, testAdhanTime!!, testIqamahTime) // the prayer and
        // adhan from the upcoming prayer method should match this

        // Creating a fake 'current' time to pass in for it to see what the upcoming prayer time is
        val currentDateTimeString = "2022-01-31T19:10:00-05:00"
        val testCurrentTime = formatter.parse(currentDateTimeString)

        val upcomingPrayerTime = PrayerDay.upcomingPrayer(prayerDays!!, testCurrentTime!!)

        if (upcomingPrayerTime != null) {
            Assert.assertEquals(testPrayerTime.prayer, upcomingPrayerTime.prayer)
            Assert.assertEquals(testPrayerTime.adhan, upcomingPrayerTime.adhan)
            Assert.assertEquals(testPrayerTime.iqamah, upcomingPrayerTime.iqamah)
            Assert.assertEquals(testPrayerTime.notificationTime(), upcomingPrayerTime.notificationTime())
        }
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