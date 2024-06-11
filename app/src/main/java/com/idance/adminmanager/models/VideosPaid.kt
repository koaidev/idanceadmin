package com.idance.adminmanager.models

import androidx.annotation.Keep

@Keep
data class VideosPaid(@Keep var list_video_paid: ArrayList<VideoPaid>?, @Keep var uid: String?){
    constructor(): this(null, null)
}