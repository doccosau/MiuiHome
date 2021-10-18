package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import de.robv.android.xposed.XposedHelpers
import android.app.Application
import android.provider.Settings
import com.yuk.miuihome.utils.ktx.*

class ModifyGoogleMinusScreen {

    fun init() {
        if (OwnSP.ownSP.getBoolean("googleMinusScreen", false)) {
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "isUseGoogleMinusScreen",
                result = true
            )
            val configCls = "com.miui.home.launcher.DeviceConfig".findClass()
            val selectMinusScreenClientId =
                configCls.getStaticObjectField("SELECT_MINUS_SCREEN_CLIENT_ID") as HashSet<String>
            selectMinusScreenClientId.add("")
            "com.miui.home.launcher.DeviceConfig".findClass().setBooleanField(
                "CAN_SWITCH_MINUS_SCREEN", true
            )
            "com.miui.home.launcher.DeviceConfig".findClass().setBooleanField(
                "ONLY_USE_GOOGLE_MINUS_SCREEN", false
            )
            val appCls = "com.miui.home.launcher.Application".findClass()

            val app = XposedHelpers.callStaticMethod(appCls, "getInstance") as Application
            "com.miui.home.launcher.DeviceConfig".findClass().setBooleanField(
                "IS_USE_GOOGLE_MINUS_SCREEN",
                "personal_assistant_google" == Settings.System.getString(
                    app.contentResolver,
                    "switch_personal_assistant"
                )
            )
        }
    }
}