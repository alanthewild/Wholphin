package com.github.damontecres.wholphin.ui.playback

import androidx.compose.ui.layout.ContentScale
import org.jellyfin.sdk.model.api.Video3dFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class AutoContentScaleTest {
    @Test
    fun nonStereoscopicKeepsTheUserDefault() {
        assertEquals(
            ContentScale.Fit,
            autoContentScaleFor(video3dFormat = null, default = ContentScale.Fit),
        )
        assertEquals(
            ContentScale.Crop,
            autoContentScaleFor(video3dFormat = null, default = ContentScale.Crop),
        )
    }

    @Test
    fun fullFrameFormatsAreSqueezedToTheDisplay() {
        assertEquals(
            ContentScale.FillBounds,
            autoContentScaleFor(Video3dFormat.FULL_SIDE_BY_SIDE, default = ContentScale.Fit),
        )
        assertEquals(
            ContentScale.FillBounds,
            autoContentScaleFor(Video3dFormat.FULL_TOP_AND_BOTTOM, default = ContentScale.Fit),
        )
    }

    @Test
    fun halfFormatsKeepTheUserDefault() {
        assertEquals(
            ContentScale.Fit,
            autoContentScaleFor(Video3dFormat.HALF_SIDE_BY_SIDE, default = ContentScale.Fit),
        )
        assertEquals(
            ContentScale.Fit,
            autoContentScaleFor(Video3dFormat.HALF_TOP_AND_BOTTOM, default = ContentScale.Fit),
        )
    }

    @Test
    fun mvcKeepsTheUserDefault() {
        // MVC cannot be decoded by the client, so there is nothing sensible to squeeze
        assertEquals(
            ContentScale.Fit,
            autoContentScaleFor(Video3dFormat.MVC, default = ContentScale.Fit),
        )
    }
}
