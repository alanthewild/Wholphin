package com.github.damontecres.wholphin.test

import com.github.damontecres.wholphin.services.DisplayMode
import com.github.damontecres.wholphin.services.RefreshRateService
import com.github.damontecres.wholphin.util.displayedVideoSize
import org.jellyfin.sdk.model.api.Video3dFormat
import org.junit.Assert
import org.junit.Test

class TestDisplayedVideoSize {
    companion object {
        // A 3D era panel: 1080p only, no UHD modes at all
        val HD_23_976 = DisplayMode(3, 1920, 1080, 23.976025f)
        val HD_24 = DisplayMode(9, 1920, 1080, 24.000002f)
        val HD_59_94 = DisplayMode(12, 1920, 1080, 59.94006f)

        val PANEL_1080P =
            listOf(HD_23_976, HD_24, HD_59_94).sortedWith(
                compareByDescending<DisplayMode>({ it.physicalWidth * it.physicalHeight })
                    .thenBy { it.refreshRateRounded },
            )
    }

    @Test
    fun twoDimensionalIsUnchanged() {
        Assert.assertEquals(1920 to 1080, displayedVideoSize(1920, 1080, null))
    }

    @Test
    fun fullSideBySideHalvesTheWidth() {
        Assert.assertEquals(
            1920 to 1080,
            displayedVideoSize(3840, 1080, Video3dFormat.FULL_SIDE_BY_SIDE),
        )
    }

    @Test
    fun fullTopAndBottomHalvesTheHeight() {
        Assert.assertEquals(
            1920 to 1080,
            displayedVideoSize(1920, 2160, Video3dFormat.FULL_TOP_AND_BOTTOM),
        )
    }

    @Test
    fun halfFormatsAreUnchanged() {
        Assert.assertEquals(
            1920 to 1080,
            displayedVideoSize(1920, 1080, Video3dFormat.HALF_SIDE_BY_SIDE),
        )
        Assert.assertEquals(
            1920 to 1080,
            displayedVideoSize(1920, 1080, Video3dFormat.HALF_TOP_AND_BOTTOM),
        )
    }

    /**
     * A 3840x1080 full side-by-side stream on a 1080p display used to match no mode at all,
     * because every candidate was filtered out by the coded width.
     */
    @Test
    fun fullSideBySideMatchesAModeOnA1080pPanel() {
        val (width, height) =
            displayedVideoSize(3840, 1080, Video3dFormat.FULL_SIDE_BY_SIDE)
        val result =
            RefreshRateService.findDisplayMode(
                displayModes = PANEL_1080P,
                streamWidth = width,
                streamHeight = height,
                targetFrameRate = 23.976025f,
                refreshRateSwitch = true,
                resolutionSwitch = false,
            )
        Assert.assertEquals(HD_23_976.modeId, result?.modeId)
    }

    @Test
    fun codedSizeMatchesNothingOnA1080pPanel() {
        val result =
            RefreshRateService.findDisplayMode(
                displayModes = PANEL_1080P,
                streamWidth = 3840,
                streamHeight = 1080,
                targetFrameRate = 23.976025f,
                refreshRateSwitch = true,
                resolutionSwitch = false,
            )
        Assert.assertNull(result)
    }
}
