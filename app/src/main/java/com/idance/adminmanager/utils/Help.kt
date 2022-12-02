package com.idance.adminmanager.utils

import com.idance.adminmanager.models.DataItem

class IdComparator : Comparator<DataItem>{
    override fun compare(o1: DataItem?, o2: DataItem?): Int {
        return if (o1?.id?.toInt() == o2?.id?.toInt())
            0
        else if (o1?.id?.toInt()!! < o2?.id?.toInt()!!)
            1
        else
            -1
    }

}