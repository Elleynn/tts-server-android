package com.elleynn.script.simple.ext

import android.view.View
import android.view.ViewGroup
import com.elleynn.common.utils.dp
import com.elleynn.common.utils.longToast
import com.elleynn.common.utils.toast
import com.elleynn.script.annotation.ScriptInterface
import splitties.init.appCtx


interface JsUserInterface {
    @ScriptInterface
    fun toast(msg: CharSequence) = appCtx.toast(msg)
    @ScriptInterface
    fun longToast(msg: CharSequence) = appCtx.longToast(msg)

    @ScriptInterface
    fun setMargins(v: View, left: Int, top: Int, right: Int, bottom: Int) {
        (v.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
            left.dp,
            top.dp,
            right.dp,
            bottom.dp
        )
    }
//
}