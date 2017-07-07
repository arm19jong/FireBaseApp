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
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult

import com.example.root.firebaseapp.MainActivityKt
import com.example.root.firebaseapp.baseclass.AccountManage
import com.example.root.firebaseapp.databinding.ActivityLoginBinding
import com.example.root.firebaseapp.login.model.SingletonAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.jvm.javaClass




/**
 * Created by root on 7/4/17.
 */
class LoginActivity: BaseActivity() , GoogleApiClient.OnConnectionFailedListener {

    private val TAG = "SignInActivity"
    private val RC_SIGN_IN = 9001
    private var mSignInButton: SignInButton? = null

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var binding: ActivityLoginBinding? = null

    private var mAuthListener: FirebaseAuth.AuthStateListener? = null


    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed:" + p0)
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        if (binding == null){
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        }

        binding!!.signInButton.setOnClickListener {  view -> signIn() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mFirebaseAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null){
                AccountManage.userID = firebaseAuth.currentUser!!.uid
                AccountManage.name = firebaseAuth.currentUser!!.displayName
                AccountManage.email = firebaseAuth.currentUser!!.email
                Log.d("TAG2", "UID: "+AccountManage.userID)
//                var f:DatabaseReference = FirebaseDatabase.getInstance().reference
                val intent = Intent(this, MainActivityKt::class.java)
                startActivity(intent)
            }
            else{
                Log.d("TAG2", "onAuthStateChanged:signed_out")}
        }
    }

    private fun handleFirebaseAuthResult(authResult: AuthResult?) {
        if (authResult != null) {
            // Welcome the user
            val user = authResult.user
            Toast.makeText(this, "Welcome " + user.email!!, Toast.LENGTH_SHORT).show()

            // Go back to the main activity
            startActivity(Intent(this, MainActivityKt::class.java))
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

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
                }
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    } else {

                    }
                }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth!!.addAuthStateListener(mAuthListener!!)
        if(this.mGoogleApiClient != null){
            this.mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAuth!!.removeAuthStateListener(mAuthListener!!)
        this.mGoogleApiClient!!.disconnect()
    }
}