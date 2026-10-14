package com.github.damontecres.wholphin.test

import com.github.damontecres.wholphin.ui.util.StreamFormatting.resolutionString
import com.github.damontecres.wholphin.util.displayedVideoSize
import com.github.damontecres.wholphin.util.video3dLabel
import org.jellyfin.sdk.model.api.Video3dFormat
import org.junit.Assert
import org.junit.Test

class FormattingTests {
    @Test
    fun testResolutionStrings() {
        Assert.assertEquals("4K", resolutionString(3840, 2160, false))
        Assert.assertEquals("1080p", resolutionString(1920, 1080, false))
        Assert.assertEquals("720p", resolutionString(1280, 720, false))
        Assert.assertEquals("480i", resolutionString(640, 480, true))

        Assert.assertEquals("576p", resolutionString(1024, 576, false))
        Assert.assertEquals("576p", resolutionString(960, 576, false))

        // 21:9
        Assert.assertEquals("1080p", resolutionString(1920, 822, false))

        Assert.assertEquals("1440p", resolutionString(2560, 1440, false))
    }

    @Test
    fun testFullFrame3dResolutionStrings() {
        // Full frame 3D is coded at twice the size of the picture that is shown, so the coded
        // size reads as 4K when both eyes are really 1080p
        val (sbsWidth, sbsHeight) = displayedVideoSize(3840, 1080, Video3dFormat.FULL_SIDE_BY_SIDE)
        Assert.assertEquals("1080p", resolutionString(sbsWidth, sbsHeight, false))

        val (tabWidth, tabHeight) =
            displayedVideoSize(1920, 2160, Video3dFormat.FULL_TOP_AND_BOTTOM)
        Assert.assertEquals("1080p", resolutionString(tabWidth, tabHeight, false))

        // Real 4K reports no 3D format, so it is unaffected
        val (uhdWidth, uhdHeight) = displayedVideoSize(3840, 2160, null)
        Assert.assertEquals("4K", resolutionString(uhdWidth, uhdHeight, false))
    }

    @Test
    fun test3dLabel() {
        Assert.assertNull(video3dLabel(null))
        Assert.assertEquals("3D FSBS", video3dLabel(Video3dFormat.FULL_SIDE_BY_SIDE))
        Assert.assertEquals("3D HSBS", video3dLabel(Video3dFormat.HALF_SIDE_BY_SIDE))
        Assert.assertEquals("3D FTAB", video3dLabel(Video3dFormat.FULL_TOP_AND_BOTTOM))
        Assert.assertEquals("3D HTAB", video3dLabel(Video3dFormat.HALF_TOP_AND_BOTTOM))
        Assert.assertEquals("3D MVC", video3dLabel(Video3dFormat.MVC))
    }
}
