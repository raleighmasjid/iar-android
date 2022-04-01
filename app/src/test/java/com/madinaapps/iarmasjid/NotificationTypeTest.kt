package com.madinaapps.iarmasjid

import org.junit.Assert
import org.junit.Test
import com.madinaapps.iarmasjid.model.NotificationType

class NotificationTypeTest {
    @Test
    fun isSaadEnumWorking() {

        val saad = NotificationType.SAADALGHAMIDI
        Assert.assertEquals("Saad al-Ghamidi", saad.title())
        Assert.assertEquals("SAAD_ALGHAMIDI", saad.channelId())
    }

    @Test
    fun isSilentEnumWorking() {
        val silent = NotificationType.SILENT
        Assert.assertEquals("Silent", silent.title())
        Assert.assertEquals("SILENT", silent.channelId())
    }
}