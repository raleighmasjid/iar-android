package com.madinaapps.iarmasjid.model

enum class Prayer {
    FAJR {
         override fun title() = "Fajr"
        override fun notificationOffset() = 0L
     },
    SHURUQ {
        override fun title() = "Shuruq"
        override fun notificationOffset() = 30L
    },
    DHUHR {
        override fun title() = "Dhuhr"
        override fun notificationOffset() = 0L
    },
    ASR {
        override fun title() = "Asr"
        override fun notificationOffset() = 0L
    },
    MAGHRIB {
        override fun title() = "Maghrib"
        override fun notificationOffset() = 0L
    },
    ISHA {
        override fun title() = "Isha"
        override fun notificationOffset() = 0L
    };

    abstract fun title(): String
    abstract fun notificationOffset(): Long // in minutes
}