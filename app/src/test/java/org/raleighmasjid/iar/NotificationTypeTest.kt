package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.model.NotificationType

class NotificationTypeTest {
    @Test
    fun isSaadEnumWorking() {

        val saad = NotificationType.SAADALGHAMIDI
        Assert.assertEquals("Saad al-Ghamidi", saad.title())
        Assert.assertEquals("SAAD_AL_GHAMIDI", saad.channelId())
    }

    @Test
    fun isSilentEnumWorking() {
        val silent = NotificationType.SILENT
        Assert.assertEquals("Silent", silent.title())
        Assert.assertEquals("SILENT", silent.channelId())
    }
}