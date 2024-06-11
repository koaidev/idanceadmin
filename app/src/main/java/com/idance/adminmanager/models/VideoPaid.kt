package com.idance.adminmanager.models

import androidx.annotation.Keep

@Keep
data class VideoPaid(
    @Keep var number_can_watch: Int?,
    @Keep var status: Boolean?,
    @Keep var uid: String?,
    @Keep var video_id: Int?,
    @Keep var name: String?,
    @Keep var thumb: String?
){
    constructor(): this(null, null, null, null, null, null)
}