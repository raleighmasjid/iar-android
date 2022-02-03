package org.raleighmasjid.iar.model

enum class Prayer {
    FAJR {
         override fun title() = "Fajr"
     },
    SHURUQ {
        override fun title() = "Shuruq"
    },
    DHUHR {
        override fun title() = "Dhuhr"
    },
    ASR {
        override fun title() = "Asr"
    },
    MAGHRIB {
        override fun title() = "Maghrib"
    },
    ISHA {
        override fun title() = "Isha"
    };

    abstract fun title(): String
}