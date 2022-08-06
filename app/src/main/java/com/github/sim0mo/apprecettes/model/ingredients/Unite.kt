package com.github.sim0mo.apprecettes.model.ingredients

enum class Unite(val coeff: Double) {
    G(1.0), KG(1000.0), MG(0.001), L(1.0), DL(0.1), CL(0.01), ML(0.001), CS(1.0), CC(1.0), O(1.0);

    override fun toString(): String {
        return if (this == O) {
            ""
        } else super.toString()
    }

    companion object {
        fun parse(s: String): Unite {
            val s1: String =
                s.replace("[.-9]| ".toRegex(), "").lowercase(java.util.Locale.getDefault())
            return when (s1) {
                "g" -> G
                "kg" -> KG
                "mg" -> MG
                "l" -> L
                "dl" -> DL
                "cl" -> CL
                "ml" -> ML
                "cs" -> CS
                "cc" -> CC
                "" -> O
                else -> throw java.lang.IllegalArgumentException(
                    String.format(
                        "Erreur dans la lecture de l'unité : %s",
                        s
                    )
                )
            }
        }

        fun parseNumber(s: String): Double {
            val n: String = s.replace("[A-z]| ".toRegex(), "")
            return n.toDouble()
        }
    }
}