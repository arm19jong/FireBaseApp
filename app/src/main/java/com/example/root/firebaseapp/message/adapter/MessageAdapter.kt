package com.example.root.firebaseapp.message.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.root.firebaseapp.CustomAdapterKt
import com.example.root.firebaseapp.R
import com.example.root.firebaseapp.baseclass.AccountManage
import com.example.root.firebaseapp.message.model.MessageModel

/**
 * Created by root on 7/6/17.
 */
class MessageAdapter(var myDataset: MutableList<MessageModel>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    val OTHER_VIEW = 1
    val MY_VIEW = 2

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var mTextView: TextView

        init {
            mTextView = v.findViewById<View>(R.id.tv) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageAdapter.ViewHolder {
        if (viewType == OTHER_VIEW){
            var v:View = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_message_other, parent, false)
            return ViewHolder(v)
        }
        else{
            var v:View = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_message_my, parent, false)
            return ViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.mTextView.setText(myDataset.get(position).text)
    }
    override fun getItemCount(): Int {
        return myDataset.size
    }
    override fun getItemViewType(position: Int): Int {
        var viewtype:Int = OTHER_VIEW
        if(myDataset.get(position).by.equals(AccountManage.userID)){
            viewtype = MY_VIEW
        }
        return viewtype
    }
    fun addItems(item:MessageModel){
//        Log.d("TAG2", "item:")
//        Log.d("TAG2", "before:" + myDataset.size)
        myDataset.add(item)
//        Log.d("TAG2", "after:" + myDataset.size)
        notifyItemRangeInserted(itemCount+1, 1)
//        notifyDataSetChanged()


    }


}