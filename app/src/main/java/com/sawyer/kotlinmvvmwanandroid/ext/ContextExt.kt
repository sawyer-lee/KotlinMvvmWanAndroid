package com.sawyer.kotlinmvvmwanandroid.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.sawyer.kotlinmvvmwanandroid.App

/**
 * 实现将特定文本复制到剪贴板的功能。
 * @param[label] User-visible label for the clip data.
 * @param[text] The actual text in the clip.
 */
fun Context.copyTextIntoClipboard(label: String? = "",text: CharSequence?) {
    if (text.isNullOrEmpty()) return
    val cbManager = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as?
            ClipboardManager ?: return
    cbManager.setPrimaryClip(ClipData.newPlainText(label,text))
}

fun String.showToast(duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(App.instance, this, duration).show()
}

/*fun Context.showToast(message: CharSequence){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}*/

fun Int.showToast(duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(App.instance, this, duration).show()
}