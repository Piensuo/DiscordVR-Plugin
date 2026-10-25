package com.github.furqanhun.discordvr

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.api.PatcherAPI
import com.de.robv.android.xposed.XC_MethodHook
import org.json.JSONObject
import java.lang.reflect.Method

@AliucordPlugin
class DiscordVR : Plugin() {

    override fun start(context: Context) {
        logger.info("DiscordVR plugin starting...")

        try {
            // Find Discord's client properties / super properties builder class
            // This hooks into the JSON map or JSONObject builder used for the gateway identify payload
            val targetClass = Class.forName("com.discord.utilities.gateway.GatewaySocket") // or equivalent Discord internal network class
            
            patcher.hook(
                targetClass.getDeclaredMethod("getIdentifyProperties"), // or the respective properties method in your Discord APK version
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val result = param.result
                        if (result is MutableMap<*, *>) {
                            @Suppress("UNCHECKED_CAST")
                            val map = result as MutableMap<String, Any>
                            
                            // Inject VR Spoofing properties matching your Python config
                            map["os"] = "Console"
                            map["browser"] = "Discord VR"
                            map["device"] = "VR-Headset"
                            map["system_locale"] = "en-US"
                            map["browser_user_agent"] = "DiscordVR/12.45"
                            map["browser_version"] = "23.7.91"
                            map["os_version"] = "10.0.45"
                            
                            logger.info("Successfully injected Discord VR client properties into gateway payload.")
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            logger.error("Failed to hook Discord gateway for VR spoofing: ${e.message}", e)
        }
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
        logger.info("DiscordVR plugin stopped.")
    }
}
