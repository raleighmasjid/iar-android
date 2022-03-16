package org.raleighmasjid.iar.model

import android.content.Context
import android.net.Uri
import android.provider.Settings
import org.raleighmasjid.iar.R

enum class NotificationType {
    SAADALGHAMIDI {
        override fun title() = "Saad al-Ghamidi"
        override fun channelId() = "SAAD_ALGHAMIDI"
        override fun soundUri(context: Context): Uri? {
            return Uri.parse(("android.resource://" + context.packageName) + "/" + R.raw.saad_alghamdi)
        }
    },
    SILENT {
        override fun title() = "Silent"
        override fun channelId() = "SILENT"
        override fun soundUri(context: Context): Uri? {
            return null
        }
    },
    SHURUQ {
        override fun title() = "Shuruq"
        override fun channelId() = "SHURUQ"
        override fun soundUri(context: Context): Uri? {
            return Settings.System.DEFAULT_NOTIFICATION_URI
        }
    };

    abstract fun title(): String
    abstract fun channelId(): String
    abstract fun soundUri(context: Context): Uri?
}