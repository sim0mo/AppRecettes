package com.github.sim0mo.apprecettes

object Utility {
    fun cleanString(s: String): String {
        var ss = s
        ss = ss
            .replace("[èéê]".toRegex(), "e")
            .replace("[àâ]".toRegex(), "a")
            .replace("ç".toRegex(), "c")
            .replace(" \\(".toRegex(), "(")
            .replace(" ".toRegex(), "_")
            .replace("'".toRegex(), "_")
        ss = ss.uppercase()
        ss = ss.trim { it <= ' ' }
        return ss
    }
}