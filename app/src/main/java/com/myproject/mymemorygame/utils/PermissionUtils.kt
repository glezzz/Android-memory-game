package com.myproject.mymemorygame.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Checks if permissions are granted or not. Return true if they've been granted, false if not.
 */
fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) === PackageManager.PERMISSION_GRANTED
}

fun requestPermission(activity: Activity?, permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
}