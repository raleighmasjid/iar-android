package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.utils.Utils
import org.raleighmasjid.iar.utils.formatToDay
import org.raleighmasjid.iar.utils.formatToTime
import org.raleighmasjid.iar.utils.isSameDay
import java.text.SimpleDateFormat

class UtilsTest {

    @Test
    fun formatDurationTest() {
        // Testing no hours, no sec
        var durationMS: Long = 1800000

        Assert.assertEquals("30 min, 0 sec", Utils.formatDuration(durationMS))

        // Testing hours
        durationMS = 18000000

        Assert.assertEquals("5 hr", Utils.formatDuration(durationMS))

        // Testing hours and minutes
        durationMS = 18956830

        Assert.assertEquals("5 hr, 15 min", Utils.formatDuration(durationMS))

        // Testing minutes and seconds
        durationMS = 935000

        Assert.assertEquals("15 min, 35 sec", Utils.formatDuration(durationMS))
    }

    @Test
    fun isSameDayTestEqual() {
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val date1String = "2022-02-01T15:21:00-05:00"
        val date2String = "2022-02-01T19:18:00-05:00"
        val date1 = formatter.parse(date1String)
        val date2 = formatter.parse(date2String)
        Assert.assertTrue(date1.isSameDay(date2))
    }

    @Test
    fun isSameDayTestUnequal() {
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val date1String = "2022-02-01T15:21:00-05:00"
        val date2String = "2022-02-02T19:18:00-05:00"
        val date1 = formatter.parse(date1String)
        val date2 = formatter.parse(date2String)
        Assert.assertFalse(date1.isSameDay(date2))
    }

    @Test
    fun formatToTimeTest() {
        // Adhan time for 'asr for February 1st, 2022 is 3:21 PM
        val correctAdhanDateTimeString = "2022-02-01T15:21:00-05:00"
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val date = formatter.parse(correctAdhanDateTimeString) // This should say:  Feb 01 15:21:00 EST 2022

        Assert.assertEquals("3:21 PM", date!!.formatToTime())
    }

    @Test
    fun formatToTimeDay() {

        // Adhan time for 'asr for February 1st, 2022 is 3:21 PM
        val correctAdhanDateTimeString = "2022-02-01T15:21:00-05:00"
        val formatter = SimpleDateFormat("y-L-d'T'HH:mm:ssX")
        val date = formatter.parse(correctAdhanDateTimeString) // This should say:  Feb 01 15:21:00 EST 2022

        Assert.assertEquals("Tue, Feb 1, 2022", date!!.formatToDay())
    }
}