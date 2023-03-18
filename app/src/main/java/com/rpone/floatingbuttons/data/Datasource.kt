package com.rpone.floatingbuttons.data

import android.util.Log
import com.rpone.floatingbuttons.models.ButtonInfo
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

@Suppress("UNREACHABLE_CODE")
class Datasource {

    fun loadInfo(): List<ButtonInfo> {

        val processBuilder = ProcessBuilder("su", "-c", "getevent -il")
        val process = processBuilder.start()
        process.waitFor()
        val output = process.inputStream.bufferedReader().readText()
        val text = output
        Log.e("text", text)

        val processBuilder1 = ProcessBuilder("su", "-c", "dumpsys input")
        val process1 = processBuilder1.start()
        process1.waitFor()
        val output1 = process1.inputStream.bufferedReader().readText()
        val text1 = output1
        Log.e("text1", text1)

        val nameRegex = Regex("""name:\s*"(.+?)"""")
        val keyRegex = Regex("KEY\\s+(\\S+(?:\\s+\\S+)*)", RegexOption.MULTILINE)
        val result = mutableListOf<Pair<String, List<String>>>()

        text.split("add device").forEach { paragraph ->
            val nameMatch = nameRegex.find(paragraph)?.groupValues?.get(1)
            val keyMatch = keyRegex.find(paragraph)?.groupValues?.get(1)

            if (nameMatch != null && keyMatch != null) {
                val nameFormatted = "${nameMatch.replace(" ", "_")}.kl"

                val keyFormatted = keyMatch.split("[\\s]+".toRegex()).map { it.trim() }

                result.add(Pair(nameFormatted, keyFormatted))
            }
        }

        val list = mutableListOf<Pair<String, List<String>>>()

        for ((path, keys) in result) {
            val newKeys = keys.toMutableList()
            newKeys.remove("(0001):")
            val inputIndex = newKeys.indexOf("input")
            if (inputIndex != -1) {
                newKeys.subList(inputIndex, newKeys.size).clear()
            }
            list.add(Pair(path, newKeys))
        }

        val regex = Regex("""\d+:\s*(\S+).*?KeyLayoutFile:\s*(\S*)""", RegexOption.DOT_MATCHES_ALL)
        val list1 = mutableListOf<String>()
        regex.findAll(text1).forEach { match ->
            var path = match.groupValues[2]
            if (path.contains("KeyCharacterMapFile:")) {
                path = path.replace("KeyCharacterMapFile:", "")
            }
            list1.add(path)
        }


        val genericLayout = "/system/usr/keylayout/generic.kl"
        val result1 = mutableListOf<Pair<String, List<String>>>()

        for (item in list) {
            var found = false
            for (layout in list1) {
                if (layout.contains(item.first)) {
                    result1.add(Pair(layout, item.second))
                    found = true
                    break
                }
            }
            if (!found) {
                result1.add(Pair(genericLayout, item.second))
            }
        }

        return result1.map { (path, names) ->
            ButtonInfo(path, names.joinToString(separator = ", "))
        }

        }
}