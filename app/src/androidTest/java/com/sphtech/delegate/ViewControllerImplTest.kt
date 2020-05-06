package com.sphtech.delegate

import com.activeandroid.ActiveAndroid
import com.activeandroid.query.Select
import com.sphtech.db.DBData
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test

import org.junit.Assert.*

class ViewControllerImplTest {

    @Test
    fun queryDataFromDB() {
        //assert(Select().from(DBData::class.java).execute<DBData>().size>=0)

    }

    @Test
    fun requestData() {
        val url = "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f&limit=60"

        val httpClient: OkHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()

        assert(response!=null)
        assert(response.body!=null)
        assert(response.body?.string()!=null)
    }

    @Test
    fun parsJsonTo() {
    }
}