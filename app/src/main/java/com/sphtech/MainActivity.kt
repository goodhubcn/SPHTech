package com.sphtech

import android.os.Bundle
import android.os.Looper
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.activeandroid.ActiveAndroid
import com.sphtech.delegate.IViewController
import com.sphtech.delegate.ViewControllerImpl
import com.sphtech.entities.MobileData
import com.sphtech.view.IViewActivity
import com.yalantis.phoenix.PullToRefreshView


class MainActivity : AppCompatActivity() , IViewActivity{

    var mLvData: ListView? = null
    var mViewController: IViewController? = null
    var mMobileAdapter: MobileAdapter? = null
    var mPullToRefreshView: PullToRefreshView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the db.
        initDB()

        setContentView(R.layout.activity_main)

        // initialize the views.
        initViews()

        // initialize the controller.
        initController()

        // request data.
        initData()
    }

    fun initViews(){
        mLvData = findViewById<ListView>(R.id.lv_data)
        var pullToRefreshView = findViewById<PullToRefreshView>(R.id.pull_to_refresh)
        pullToRefreshView.setOnRefreshListener {
            mViewController?.requestData()
        }
        mPullToRefreshView = pullToRefreshView

        mMobileAdapter = MobileAdapter(this, R.layout.mobile_data_item)
        mLvData?.adapter = mMobileAdapter
//        mMobileAdapter?.notifyDataSetChanged()
    }

    fun initDB(){
//        val dbConfiguration: Configuration = Configuration.Builder(this.applicationContext).setDatabaseName("sphtech.db").create()
//        ActiveAndroid.initialize(dbConfiguration)
        ActiveAndroid.initialize(this)
    }

    fun initController(){
        mViewController = ViewControllerImpl(this)
    }

    fun initData(){
        mViewController?.requestData()
    }

    // View delegate
    override fun updateList(list: List<MobileData>) {

//        var mobileData = MobileData()
//        mobileData.hasDesc = false
//        mobileData.quarter = "马勤军"
//        mobileData.data = "m.jackie@aliyun.com"
//
//        var tempList = ArrayList<MobileData>()
//        tempList.add(mobileData)
//        tempList.addAll(list)

        if (Thread.currentThread() == Looper.getMainLooper().thread){
            mMobileAdapter?.updateData(list)
            mPullToRefreshView?.setRefreshing(false)
        }else{
            mPullToRefreshView?.post {
                mMobileAdapter?.updateData(list)
                mPullToRefreshView?.setRefreshing(false)
            }
        }
    }

//    override fun onTerminate() {
//        super.onTerminate()
//        ActiveAndroid.dispose()
//    }

    override fun onDestroy() {
        super.onDestroy()
        mViewController = null
        ActiveAndroid.dispose()
    }
}
