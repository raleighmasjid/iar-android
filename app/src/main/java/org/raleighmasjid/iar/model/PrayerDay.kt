package org.raleighmasjid.iar.model

import java.util.*

data class PrayerDay(
    val date: Date,
    val hijri: Hijri,
    val adhan: AdhanSchedule,
    val iqamah: IqamahSchedule
)