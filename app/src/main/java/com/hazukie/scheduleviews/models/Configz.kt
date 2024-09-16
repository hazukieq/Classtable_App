package com.hazukie.scheduleviews.models

import com.google.gson.Gson

open class Configz(val uid:String,var name:String){
    val gson=Gson()

    fun toObj(string: String="{}"):Configz{
        return gson.fromJson(string,Configz::class.java)
    }

    fun toJson(configz: Configz=Configz("","")):String{
        return gson.toJson(configz)
    }

    fun getFormat():String{
        return name.replace(".txt","")
    }

    fun isDuplicate(mutableList: MutableList<Configz>):Boolean{
        var isDuplicated=false
        for(item in mutableList){
            if(item.name==name){
                isDuplicated=true
                break
            }
        }
        return isDuplicated
    }

    fun rename(neo:String):Int{
        val neoName:String=neo.trim()

        //statusCode: 0 represents failed(A!=B),1 represents success,2 means empty
        val code=when{
            neoName.isNotBlank()->1
            neoName.isBlank()->0
            else -> 1
        }

        if(code==1&&neoName!=name) name=neoName

        return code
    }
}

class TimeConfig(uid: String,name: String):Configz(uid,name){
    val description:String

    fun getDescript():String{
        return ""
    }
    init {
        description=getDescript()
    }
}
