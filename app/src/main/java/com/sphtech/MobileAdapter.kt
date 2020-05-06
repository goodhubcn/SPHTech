package com.sphtech

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.sphtech.entities.MobileData

class MobileAdapter : ArrayAdapter<MobileData> {

    var mList: List<MobileData>? = ArrayList<MobileData>()
    var mResourceId = 0

    constructor(context: Context, resource: Int, list:List<MobileData>) : this(context, resource) {
        mList = list
        mResourceId = resource
    }

    constructor(context: Context, resource: Int) : super(context, resource) {
        mResourceId = resource
    }

    fun updateData(list: List<MobileData>){
        mList = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mList?.size!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return super.getView(position, convertView, parent)

        var data = mList?.get(position)

        var view: View = LayoutInflater.from(context).inflate(mResourceId, parent, false)
        view.findViewById<TextView>(R.id.tv_quarter).setText(data?.quarter)
        view.findViewById<TextView>(R.id.tv_data).setText(data?.data.toString())
        var bt: Button = view.findViewById<Button>(R.id.btn_go)
        if(data?.hasDesc!!){
            bt.visibility = View.VISIBLE
            bt.setOnClickListener(){
                Toast.makeText(context, "details: ${data?.quarter} -> ${data?.data}", Toast.LENGTH_SHORT).show()
            }
        }else{
            bt.visibility = View.GONE
        }

        return view;
    }
}