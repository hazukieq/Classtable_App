package com.hazukie.scheduleviews.models

data class OldClassLabel
(
    var class_nums: Int,
    var start_class: Int,
    var week: Int,
    var detail_time: Int,
    var subject: String="",
    var clRoom: String="",
    var planotes: String="",
    var color: Int
    )