package com.hazukie.scheduleviews.models

//var title: String, var subtitle: String, var description: String
data class HrionzModel(val scheuid: String,var schename:String,var timeConfig:TimeConfig) {
    val timename:String=timeConfig.name
    val timeuid:String=timeConfig.uid
    val descript=timeConfig.description
}