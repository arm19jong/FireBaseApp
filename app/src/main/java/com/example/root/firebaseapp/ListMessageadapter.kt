package com.example.root.firebaseapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.root.firebaseapp.baseclass.BaseKey
import com.example.root.firebaseapp.message.MessageActivity

/**
 * Created by root on 7/6/17.
 */
class ListMessageadapter(var myDataset: MutableList<Map<String, String>>) : RecyclerView.Adapter<ListMessageadapter.ViewHolder>() {

    var context:Context?=null

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var mTextView: TextView

        init {
            mTextView = v.findViewById<View>(R.id.tv) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v:View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_two, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.mTextView.text = myDataset.get(position).getValue("name")
        holder!!.mTextView.setOnClickListener(View.OnClickListener { view -> onClick(myDataset.get(position).getValue("id"),  myDataset.get(position).getValue("name")) })

    }

    fun onClick(id:String, name:String){
        val intent = Intent(context, MessageActivity::class.java)
        intent.putExtra(BaseKey.MESSAGE_ID, id)
        intent.putExtra(BaseKey.MESSAGE_NAME, name)
        context!!.startActivity(intent)

    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    fun addItems(item:Map<String, String>){
//        Log.d("TAG2", "item:")
//        Log.d("TAG2", "before:" + myDataset.size)
        myDataset.add(item)
//        Log.d("TAG2", "after:" + myDataset.size)
        notifyItemRangeInserted(itemCount+1, 1)


    }

}