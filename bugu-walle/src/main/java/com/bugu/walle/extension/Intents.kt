package com.bugu.walle.extension

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

fun Context.isIntentAvailable(intent: Intent?): Boolean {
    return packageManager
        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .size > 0
}

fun Context.launchAppDetailsSettings() {
    val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:$packageName")
    }
    if (!isIntentAvailable(intent)) return
    startActivity(intent)
}

fun getShareTextIntent(content: String?): Intent? {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, content)
    return getIntent(intent, true)
}

fun Context.shareText(content: String){
    startActivity(getShareTextIntent(content))
}

private fun getIntent(intent: Intent, isNewTask: Boolean): Intent? {
    return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
}

fun startDevelopmentActivity(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val componentName = ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings")
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.component = componentName
            intent.action = "android.intent.action.View"
            context.startActivity(intent)
        } catch (e1: Exception) {
            try {
                //部分小米手机采用这种方式跳转
                val intent = Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
        }
    }
}

fun startLocalActivity(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}