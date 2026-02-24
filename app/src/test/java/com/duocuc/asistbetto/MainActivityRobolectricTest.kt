package com.duocuc.asistbetto

import com.duocuc.asistbetto.MainActivity
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class MainActivityRobolectricTest {

    @Test
    fun `MainActivity se crea sin crash`() {
        val controller = Robolectric.buildActivity(MainActivity::class.java)
        val activity = controller.setup().get()

        assertNotNull(activity)
    }
}