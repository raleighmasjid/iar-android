package com.madinaapps.iarmasjid

import org.junit.Assert
import org.junit.Test
import com.madinaapps.iarmasjid.model.json.Hijri

class HijriTest {

    @Test
    fun isfomatted() {

        val randomHijriDate = Hijri(2, 8, 1443, "Sha'aban")
        Assert.assertEquals("Sha'aban 2, 1443 h", randomHijriDate.fomatted())
    }
}