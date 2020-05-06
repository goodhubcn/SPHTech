package com.sphtech.entities

class MobileNetData (val result: MobileNetData.Result){
     val help: String = ""
     val success: Boolean = false
//    val result: com.sphtech.entities.MobileNetData.Result

     class Result(val records: ArrayList<Record>) {
         val resource_id: String = ""
         val limit:Int = 0
         val total:Int = 0
     }
     class Record(){
         val volume_of_mobile_data: String = ""
         val _id:Int = 0
         val quarter: String = ""
     }
}