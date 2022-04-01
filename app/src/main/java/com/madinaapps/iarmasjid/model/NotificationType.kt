package com.madinaapps.iarmasjid.model

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.Settings

enum class NotificationType {
    SAADALGHAMIDI {
        override fun title() = "Saad Al-Ghamdi"
        override fun channelId() = namespacedId("SAAD_ALGHAMIDI")
        override fun soundUri(context: Context): Uri? {
            return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/saad_alghamdi")
        }
    },
    ALAFASY {
        override fun title() = "Mishary Alafasy"
        override fun channelId() = namespacedId("ALAFASY")
        override fun soundUri(context: Context): Uri? {
            return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/alafasy")
        }
    },
    SILENT {
        override fun title() = "Silent"
        override fun channelId() = namespacedId("SILENT")
        override fun soundUri(context: Context): Uri? {
            return null
        }
    },
    SHURUQ {
        override fun title() = "Shuruq"
        override fun channelId() = namespacedId("SHURUQ")
        override fun soundUri(context: Context): Uri? {
            return Settings.System.DEFAULT_NOTIFICATION_URI
        }
    };

    companion object {
        fun options(): List<NotificationType> {
            return NotificationType.values().filter { it != SHURUQ }
        }

        fun namespacedId(id: String): String {
            return "org.raleighmasjid.iar.b1.$id"
        }
    }

    abstract fun title(): String
    abstract fun channelId(): String
    abstract fun soundUri(context: Context): Uri?
}