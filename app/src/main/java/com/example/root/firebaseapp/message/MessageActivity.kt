package com.example.root.firebaseapp.message

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.root.firebaseapp.ListMessageadapter
import com.example.root.firebaseapp.R
import com.example.root.firebaseapp.baseclass.AccountManage
import com.example.root.firebaseapp.baseclass.BaseActivity
import com.example.root.firebaseapp.baseclass.BaseKey
import com.example.root.firebaseapp.databinding.ActivityMessageBinding
import com.example.root.firebaseapp.message.adapter.MessageAdapter
import com.example.root.firebaseapp.message.model.MessageModel
import com.google.firebase.database.*
import java.util.*

/**
 * Created by root on 7/6/17.
 */
class MessageActivity :BaseActivity() {
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mAdapter:MessageAdapter
    var binding:ActivityMessageBinding? = null
    var item:MutableList<MessageModel> = ArrayList<MessageModel>()
    private lateinit var rootRef: DatabaseReference
    private var friendRef: DatabaseReference? = null
    private var conRef: DatabaseReference? = null
    private var accountRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(binding == null){
            binding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        }
        title = intent.getStringExtra(BaseKey.MESSAGE_NAME)
        setRecycleView()
        setRef()
        callServiceMessage()
        binding!!.send.setOnClickListener(View.OnClickListener { view -> callSendMessage(binding!!.editMessage.text.toString()) })
        binding!!.editMessage.setOnClickListener(View.OnClickListener { view ->  onEditClick()})

    }
    fun onEditClick(){
//        binding!!.myRecyclerView.scrollToPosition(mAdapter.getItemCount()-1)
//        val handler = Handler()
//        handler.postDelayed({
//            //Do something after 500ms
//
//        }, 500)

    }

    fun callSendMessage(message:String){
        Log.d("TAG2", message)
        binding!!.editMessage.text.clear()
        var time: Date = Date()
        var param:MutableMap<String, Any?> = HashMap<String, Any?>()
        param.put("by", AccountManage.userID!!)
        param.put("text", message)
        param.put("time", Date().time)
        conRef!!.child(intent.getStringExtra(BaseKey.MESSAGE_ID)).push().setValue(param)

    }
    fun callServiceMessage(){
        val messageListener = object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var message:MessageModel? = p0!!.getValue(MessageModel::class.java)
                Log.d("TAG2", message!!.text)
                addRecycleView(message)
                binding!!.myRecyclerView.scrollToPosition(mAdapter.getItemCount()-1)


            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        conRef!!.child(intent.getStringExtra(BaseKey.MESSAGE_ID)).addChildEventListener(messageListener)

    }

    fun setRecycleView(){

        mLayoutManager = LinearLayoutManager(this)
        (mLayoutManager as LinearLayoutManager).setStackFromEnd(true)
////        mLayoutManager.
        binding!!.myRecyclerView.layoutManager = mLayoutManager

        mAdapter = MessageAdapter(item)
        binding!!.myRecyclerView.adapter = mAdapter
    }
    fun setRef(){
        rootRef = FirebaseDatabase.getInstance().getReference()
        friendRef = rootRef.child("message").child("account").child(AccountManage.userID)
        conRef = rootRef.child("message").child("conver")
        accountRef = rootRef.child("account")
    }
    fun addRecycleView(item:MessageModel){
        mAdapter.addItems(item)

    }

}