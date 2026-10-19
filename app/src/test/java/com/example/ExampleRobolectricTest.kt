package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.ScenariosData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SpotCheck Media Detective", appName)
    }

    @Test
    fun `scenarios data loads cases correctly`() {
        val cases = ScenariosData.sampleCases
        assertTrue("Scenarios should not be empty", cases.isNotEmpty())
        assertTrue("Every case should have detectable clues", cases.all { it.clues.isNotEmpty() })
        assertTrue("Every case should have comments", cases.all { it.comments.isNotEmpty() })
    }
}
