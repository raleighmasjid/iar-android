package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerTime
import java.text.SimpleDateFormat
import java.time.Instant

class PrayerTimeTest {

    @Test
    fun isNotificationTimeAccurate() {

        // March 6th, 2022, 6:18 PM
        val dateString = "2022-03-06T18:18:00-05:00"
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val adhan = formatter.parse(dateString) // This should say: Sun Mar 06 18:18:00 EST 2022

        val prayer = Prayer.MAGHRIB
        val prayerTime = PrayerTime(prayer, adhan!!, null)

        val correctValue = Instant.parse("2022-03-06T23:18:00Z")
        Assert.assertEquals(correctValue, prayerTime.notificationTime())
    }
}