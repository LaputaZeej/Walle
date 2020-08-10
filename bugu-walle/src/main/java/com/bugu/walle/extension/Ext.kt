package com.bugu.walle.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.text.SimpleDateFormat

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
    get() = "${versionExt}/${screenWidth}*${screenHeight}/${statusBarHeight}\n" +
            "${Build.MANUFACTURER}/${Build.MODEL}/${Build.DEVICE}/${Build.DISPLAY}/${Build.VERSION.SDK_INT}\n"

val Context.appInfoForCopy: String
    get() = "【Version】${versionExt}\n" +
            "【Screen】${screenWidth}*${screenHeight} - ${statusBarHeight}\n" +
            "【MANUFACTURER】${Build.MANUFACTURER}\n" +
            "【MODEL】${Build.MODEL}\n" +
            "【DEVICE】${Build.DEVICE}\n" +
            "【DISPLAY】${Build.DISPLAY}\n" +
            "【SDK_INT】${Build.VERSION.SDK_INT}\n"


val Context.versionExt: String
    get() = packageManager.getPackageInfo(packageName, 0).run {
        "$packageName $versionName/$versionCode "
    }

val View.visibleExt: Boolean
    get() = visibility == View.VISIBLE

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

abstract class TextWatcherAdapter : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}


