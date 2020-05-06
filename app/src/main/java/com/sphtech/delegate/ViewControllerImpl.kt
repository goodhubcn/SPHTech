package com.sphtech.delegate

import android.util.JsonReader
import android.util.Log
import com.activeandroid.query.Select
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.sphtech.db.DBData
import com.sphtech.entities.MobileData
import com.sphtech.entities.MobileNetData
import com.sphtech.view.IViewActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.StringReader
import java.util.concurrent.Executors

class ViewControllerImpl : IViewController{

    val TAG = "ViewControllerImpl"

    var mHasDBData = false

    var mDelegate: IViewActivity? = null
    val mExecutors = Executors.newFixedThreadPool(3)

    constructor(delegate: IViewActivity){
        mDelegate = delegate
    }

    fun queryDataFromDB(){
        val data = Select().from(DBData::class.java).execute<DBData>()
        var allData: ArrayList<MobileData> = arrayListOf()
        for (line in data){
            // Response json data to list for show.
            var list: List<MobileData> = parsJsonTo(line.data)
            allData.addAll(list)
        }
        if(allData.isNotEmpty()){
            mHasDBData = true
            mDelegate?.updateList(allData)
        }else{
            mHasDBData = false
        }
    }

    override fun requestData() {
        // First query data form database.
        queryDataFromDB();

        // Second request data form network.
        mExecutors.execute(){
            val url = "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f&limit=60"

            val httpClient: OkHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(url).build()

            val response = httpClient.newCall(request).execute()
            if(response?.body != null){
                val responseData = response.body!!.string()

                if (!mHasDBData){
                    // Save data to db.
                    var resData = DBData()
                    resData.data = responseData
                    resData.save()
                }

                // Response json data to list for show.
                var list: List<MobileData> = parsJsonTo(responseData)

                if(list.isNotEmpty()){
                    mDelegate?.updateList(list)
                }
            }else{
                Log.e(TAG, "response null")
            }

        }
    }

    override fun nextPageData() {
        TODO("Not yet implemented")
    }

    fun parsJsonTo(data: String): List<MobileData>{

        Log.i(TAG, " result: $data")

        var result: ArrayList<MobileData> = ArrayList()

        var klaxon = Klaxon()
        var rets = klaxon.parse<MobileNetData>(data)
        val records = rets?.result?.records

        var firstYearObj: String? = null

        var valueTotal = 0.0f
        var privValue = 0.0f
        var hasDesc = false

        if (records != null) {
            for (record in records){

                val splitRet =  record.quarter.split("-")
                val year = splitRet[0]
                val month = splitRet[1]
                if(firstYearObj == null){
                    firstYearObj = year
                    privValue = record.volume_of_mobile_data.toFloat()
                    valueTotal += privValue
                }else if(firstYearObj.equals(year)){
                    if(privValue > record.volume_of_mobile_data.toFloat()){
                        hasDesc = true
                    }
                    valueTotal += record.volume_of_mobile_data.toFloat()
                    privValue = record.volume_of_mobile_data.toFloat()
                }else{
                    var mobileData = MobileData()
                    mobileData.data = valueTotal.toString()
                    mobileData.quarter = firstYearObj
                    mobileData.id = 0
                    mobileData.hasDesc = hasDesc
                    result.add(mobileData)

                    firstYearObj = year
                    privValue = record.volume_of_mobile_data.toFloat()
                    valueTotal = privValue
                    hasDesc = false
                }
            }

            // Add the last volume.
            var mobileData = MobileData()
            mobileData.data = valueTotal.toString()
            mobileData.quarter = firstYearObj
            mobileData.id = 0
            mobileData.hasDesc = hasDesc
            result.add(mobileData)

        }

        return result
    }
}