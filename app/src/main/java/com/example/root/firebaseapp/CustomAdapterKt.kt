package com.example.root.firebaseapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by root on 7/3/17.
 */
class CustomAdapterKt(var myDataset: MutableList<String>) : RecyclerView.Adapter<CustomAdapterKt.ViewHolder>() {
    val OTHER_VIEW = 21
    val MY_VIEW = 22



    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var mTextView: TextView

        init {
            mTextView = v.findViewById<View>(R.id.tv) as TextView
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomAdapterKt.ViewHolder{
        if(viewType == OTHER_VIEW){
            var v:View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_one, parent, false)
            var vh:ViewHolder = ViewHolder(v)
            return vh
        }

        else{
            val v:View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_two, parent, false)
            val vh:ViewHolder = ViewHolder(v)
            return vh
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.setText(myDataset.get(position))
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype:Int = OTHER_VIEW
        if(Integer.parseInt(myDataset[position]) % 2 == 0){
            viewtype = MY_VIEW
        }
        return viewtype
    }
}