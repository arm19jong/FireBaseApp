package com.example.root.firebaseapp.login.model

import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by root on 7/4/17.
 */
public class SingletonAuth private constructor() {
    init{}

    companion object {
        var mGoogleApiClient: GoogleApiClient? = null
        var sss: String? = null

    }


}