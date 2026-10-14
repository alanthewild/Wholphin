package com.github.damontecres.wholphin.util

import org.jellyfin.sdk.model.api.Video3dFormat

/**
 * The size of the picture that is actually shown on the display.
 *
 * Full frame 3D packs both eyes into one frame at their native size, so the coded size is twice
 * the size of the picture the display shows. A full side-by-side 1080p film is coded as
 * 3840x1080 but is still a 1080p picture.
 *
 * Anything that reasons about what the viewer sees, such as display mode selection or the
 * resolution shown in the UI, has to use this rather than the coded size. Half formats are
 * already stored at the displayed size and are left alone.
 */
fun displayedVideoSize(
    width: Int,
    height: Int,
    video3dFormat: Video3dFormat?,
): Pair<Int, Int> =
    when (video3dFormat) {
        Video3dFormat.FULL_SIDE_BY_SIDE -> width / 2 to height
        Video3dFormat.FULL_TOP_AND_BOTTOM -> width to height / 2
        else -> width to height
    }

/**
 * The label shown for stereoscopic content, or null for 2D.
 *
 * jellyfin-web shows a plain "3D" for any non-null Video3DFormat, but the packing is worth
 * knowing before you start watching: a full frame source carries a whole 1080p picture per eye,
 * a half one carries half the horizontal or vertical resolution per eye.
 */
fun video3dLabel(video3dFormat: Video3dFormat?): String? =
    when (video3dFormat) {
        Video3dFormat.FULL_SIDE_BY_SIDE -> "3D FSBS"
        Video3dFormat.HALF_SIDE_BY_SIDE -> "3D HSBS"
        Video3dFormat.FULL_TOP_AND_BOTTOM -> "3D FTAB"
        Video3dFormat.HALF_TOP_AND_BOTTOM -> "3D HTAB"
        Video3dFormat.MVC -> "3D MVC"
        null -> null
    }
