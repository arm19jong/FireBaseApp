package com.example.root.firebaseapp.login

import android.os.Bundle
import android.util.Log
import com.example.root.firebaseapp.R
import com.example.root.firebaseapp.baseclass.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.SignInButton
import android.content.Intent
import android.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult

import com.example.root.firebaseapp.MainActivityKt
import com.example.root.firebaseapp.databinding.ActivityLoginBinding
import com.example.root.firebaseapp.login.model.SingletonAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.jvm.javaClass




/**
 * Created by root on 7/4/17.
 */
class LoginActivity: BaseActivity() , GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("TAG2", "fail")
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
//    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mGoogleApiClient = SingletonAuth.mGoogleApiClient
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        if (binding == null) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        }

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        mGoogleApiClient!!.connect()

//        mGoogleApiClient = getApiClient()



        init()
    }

    private fun init(){

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD)
        binding.signInButton.setOnClickListener { view -> signIn() }
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null){
                SingletonAuth.mGoogleApiClient = mGoogleApiClient
//                mGoogleApiClient!!.clearDefaultAccountAndReconnect()
                Log.d("TAG2", "set SSS")
                SingletonAuth.sss = "sample"
                Log.d("TAG2", "onAuthStateChanged:signed_in:" + firebaseAuth.currentUser)
                val intent = Intent(this, MainActivityKt::class.java)
                startActivity(intent)
            }
            else{
                Log.d("TAG2", "onAuthStateChanged:signed_out")}
        }
    }

    val  RC_SIGN_IN: Int = 1

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                if (account != null) {
                    firebaseAuthWithGoogle(account)
//                    SingletonAuth.mGoogleApiClient = mGoogleApiClient
//                    Log.d("TAG2", "set SSS")
//                    SingletonAuth.sss = "sample"
                }
            } else {
                // Google Sign In failed, update UI appropriately
            }
        }

    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
        if(this.mGoogleApiClient != null){
            this.mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)
        this.mGoogleApiClient!!.disconnect()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG2", "signInWithCredential:success")
//                        val user = mAuth.currentUser
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("TAG2", "signInWithCredential:failure", task.exception)
//                        Toast.makeText(this@GoogleSignInActivity, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }

                    // ...
                }
    }

}