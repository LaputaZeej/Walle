package com.bugu.walle.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.text.SimpleDateFormat
import java.util.*

val Context.windowManagerService: WindowManager
    get() = getSystemService(WINDOW_SERVICE) as WindowManager

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels


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

fun WindowManager.addViewExt(view: View, param: WindowManager.LayoutParams) {
    this.addView(view, param.apply {
        // 在Android 8.0之前，悬浮窗口设置可以为TYPE_PHONE，这种类型是用于提供用户交互操作的非应用窗口。
        // 而Android 8.0对系统和API行为做了修改，包括使用SYSTEM_ALERT_WINDOW权限的应用无法再使用一下窗口类型来在其他应用和窗口上方显示提醒窗口
        // - TYPE_PHONE
        // - TYPE_PRIORITY_PHONE
        // - TYPE_SYSTEM_ALERT
        // - TYPE_SYSTEM_OVERLAY
        // - TYPE_SYSTEM_ERROR
        // 如果需要实现在其他应用和窗口上方显示提醒窗口，那么必须该为TYPE_APPLICATION_OVERLAY的新类型
        param.type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE
    })
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModels(factory: ViewModelProvider.Factory? = null) =
    factory?.let {
        ViewModelProvider(this, it).get(T::class.java)
    } ?: ViewModelProvider(this).get(T::class.java)


@SuppressLint("SimpleDateFormat")
val SDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss")
val SDF_SIMPLE = SimpleDateFormat("HH:mm:ss sss")

fun formatDate(time: Long, detail: Boolean = false): String = detail.run {
    if (this) SDF else SDF_SIMPLE
}.format(time)

val Context.appInfo:String
        get() = "${Build.MANUFACTURER}/${Build.MODEL}/${Build.DEVICE}/${Build.DISPLAY}/${versionExt}/${Build.VERSION.SDK_INT}"

val Context.versionExt:String
    get() = "${packageManager.getPackageInfo(packageName,0).run { 
        "$versionName/$versionCode "
    }}"


