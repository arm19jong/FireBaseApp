package com.example.root.firebaseapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.root.firebaseapp.login.model.SingletonAuth
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

/**
 * Created by root on 7/3/17.
 */
class MainActivityKt : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<*>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var myDataset: MutableList<String>
    private lateinit var mAuth: FirebaseAuth
    private var mGoogleApiClient = SingletonAuth.mGoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.my_recycler_view) as RecyclerView

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        myDataset = ArrayList<String>()

        for (i in 0..20) {
            myDataset.add(i.toString() + "")
        }

        mAdapter = CustomAdapterKt(myDataset)
        mRecyclerView.adapter = mAdapter

        mAuth = FirebaseAuth.getInstance()
        Toast.makeText(this@MainActivityKt, mAuth.currentUser!!.email, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_toolbar_item, menu)
        var menuItem: MenuItem = menu!!.findItem(R.id.action_log_out)
        menuItem.setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_log_out -> {logout(); return true;}
            else -> {return super.onOptionsItemSelected(item)}


        }
    }

    fun logout(){

//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { status -> mAuth.sign}
//        Auth.GoogleSignInApi.signOut((SingletonAuth.instance.mGoogleApiClient)).setResultCallback(ResultCallback {  })
//        var sss = SingletonAuth.sss
//        if(sss == null){
//            Log.d("TAG2", "null")
//        }
//        else{
//            Log.d("TAG2", sss.toString())
//        }

//        onStop();onStart();onStart();
        Log.d("TAG2", "Connect: "+mGoogleApiClient!!.isConnected)
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
//        mGoogleApiClient!!.clearDefaultAccountAndReconnect()
        mAuth.signOut()
        FirebaseAuth.getInstance().signOut()

        finish()
    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        Log.d("TAG2", "OnStart Connect: "+mGoogleApiClient!!.isConnected)
        super.onStart()

    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        Log.d("TAG2", "OnStop Connect: "+mGoogleApiClient!!.isConnected)
        super.onStop()

    }
}