package org.raleighmasjid.iar

import org.junit.Assert
import org.junit.Test
import org.raleighmasjid.iar.model.Hijri

class HijriTest {

    @Test
    fun isfomatted() {

        val randomHijriDate = Hijri(2, 8, 1443, "Sha'aban")
        Assert.assertEquals("Sha'aban 2, 1443 h", randomHijriDate.fomatted())
    }
}