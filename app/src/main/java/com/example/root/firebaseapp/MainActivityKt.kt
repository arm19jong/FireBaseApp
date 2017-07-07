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
import com.example.root.firebaseapp.baseclass.AccountManage
import com.example.root.firebaseapp.login.model.SingletonAuth
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by root on 7/3/17.
 */
class MainActivityKt : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ListMessageadapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var myDataset: MutableList<String>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleApiClient:GoogleApiClient
    private lateinit var rootRef:DatabaseReference
    private var friendRef:DatabaseReference? = null
    private var accountRef:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecycleView()

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        mGoogleApiClient.context
        Toast.makeText(this@MainActivityKt, mAuth.currentUser!!.email, Toast.LENGTH_SHORT).show()
        setRef()
        callServiceAllConver()
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

        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
//        mGoogleApiClient!!.clearDefaultAccountAndReconnect()
        mAuth.signOut()
        FirebaseAuth.getInstance().signOut()

        finish()
    }

    var item:MutableList<Map<String, String>> = ArrayList<Map<String, String>>()
    fun callServiceAllConver(){
//        friendRef.addListenerForSingleValueEvent()
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A new comment has been added, add it to the displayed list
//                val comment = dataSnapshot.getValue<Comment>(Comment::class.java)
//                Log.d("TAG2", dataSnapshot.key+": "+dataSnapshot.value)
                // callName
                accountRef!!.child(dataSnapshot.key).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot?) {
                        Log.d("TAG2", p0!!.child("name").value.toString()+": "+dataSnapshot.value.toString())
                        val param = java.util.HashMap<String, String>()
                        param.put("name", p0!!.child("name").value.toString())
                        param.put("id", dataSnapshot.value.toString())
//                        item.add(param)
                        addRecycleView(param)
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed, use the key
                // to determine if we are displaying this
                // comment and if so displayed the changed comment.
//                val newComment = dataSnapshot.getValue<Comment>(Comment::class.java)
//                val commentKey = dataSnapshot.key
                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // A comment has changed, use the key
                // to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key
                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed position,
                // use the key to determine if we are
//                 displaying this comment and if so move it.
//                val movedComment = dataSnapshot.getValue<Comment>(Comment::class.java)
//                val commentKey = dataSnapshot.key
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(mContext, "Failed to load comments.",
//                        Toast.LENGTH_SHORT).show()
            }
        }
        friendRef!!.addChildEventListener(childEventListener)
    }

    fun setRef(){
        rootRef = FirebaseDatabase.getInstance().getReference()
        friendRef = rootRef.child("message").child("account").child(AccountManage.userID)
        accountRef = rootRef.child("account")
    }

    fun setRecycleView(){
        mRecyclerView = findViewById(R.id.my_recycler_view) as RecyclerView

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        mAdapter = ListMessageadapter(item)
        mRecyclerView.adapter = mAdapter
        mAdapter.context = this@MainActivityKt
    }
    fun addRecycleView(item:Map<String, String>){
        mAdapter.addItems(item)
    }
}