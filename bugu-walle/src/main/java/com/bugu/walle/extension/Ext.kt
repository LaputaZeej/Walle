package com.bugu.walle.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bugu.walle.TAG
import java.text.SimpleDateFormat
import java.util.*

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * 状态栏高度
 */
val Context.statusBarHeight: Int
    get() = resources.getDimensionPixelSize(
        resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
    )

fun Fragment.requestOverlayPermission(requestCode: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        startActivityForResult(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${requireActivity().packageName}")
            ), requestCode
        )
    }
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModels(factory: ViewModelProvider.Factory? = null) =
    factory?.let {
        ViewModelProvider(this, it).get(T::class.java)
    } ?: ViewModelProvider(this).get(T::class.java)


@SuppressLint("SimpleDateFormat")
val SDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss")

@SuppressLint("SimpleDateFormat")
val SDF_SIMPLE = SimpleDateFormat("HH:mm:ss sss")

fun formatDate(time: Long, detail: Boolean = false): String = detail.run {
    if (this) SDF else SDF_SIMPLE
}.format(time)

val Context.appInfo: String
    get() = "${Build.MANUFACTURER}/${Build.MODEL}/${Build.DEVICE}/${Build.DISPLAY}/${versionExt}/${Build.VERSION.SDK_INT}/${screenWidth}*${screenHeight}/${statusBarHeight}"

val Context.versionExt: String
    get() = packageManager.getPackageInfo(packageName, 0).run {
        "$versionName/$versionCode "
    }


