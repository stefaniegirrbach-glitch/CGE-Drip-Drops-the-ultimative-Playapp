package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CGE Drip Drop", appName)
    }

    @Test
    fun `game viewmodel tap and upgrade logic`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = GameViewModel(app)

        val initialDiamonds = viewModel.uiState.value.diamonds
        assertTrue("Initial diamonds should be positive", initialDiamonds > 0)

        // Tap ice cream
        viewModel.onIceCreamTapped(180f, 150f)
        assertTrue("Diamonds should increase after tap", viewModel.uiState.value.diamonds > initialDiamonds)

        // Buy upgrade
        val upgradeId = "bigger_ice_cream"
        val prevLevel = viewModel.uiState.value.biggerIceCreamLevel
        viewModel.buyUpgrade(upgradeId)
        assertEquals("Upgrade level should increment", prevLevel + 1, viewModel.uiState.value.biggerIceCreamLevel)
    }
}
