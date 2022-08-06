package com.github.sim0mo.apprecettes.model.ingredients

import com.github.sim0mo.apprecettes.model.Main
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer

/**
 * Ingrédient : a un nom qui doit être contenu dans ingredients.txt
 * Peut avoir une spécification qui n'a pas d'influence sur l'égalité
 */
class Ingredient {
    val name: String
    get() {
        return if (specification == "") field else field + specification
    }
    var specification = ""
        private set

    constructor(nom: String) {
        name = nom
    }

    constructor(nom: String, specification: String) {
        name = nom
        this.specification = specification
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if ((other == null) || (javaClass != other.javaClass)) return false
        val that = other as Ingredient
        return name == that.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        private val allowedIngredients: MutableSet<String> = HashSet()
        fun fromString(s: String): Ingredient {
            var s = s
            s = Main.cleanString(s)
            val result: Ingredient = if (s.contains("(")) {
                Ingredient(
                    s.substring(0, s.indexOf("(")),
                    s.substring(s.indexOf("("), s.indexOf(")") + 1)
                )
            } else {
                Ingredient(s)
            }
            require(allowedIngredients.contains(result.name)) {
                String.format(
                    "Aucun ingrédient correspondant à <<%s>> n'est répertorié.",
                    result.name
                )
            }
            return result
        }

        init {
            try {
                val reader = BufferedReader(
                    InputStreamReader(
                        Objects.requireNonNull(
                            Ingredient::class.java.classLoader.getResourceAsStream(
                                "ingredients.txt"
                            )
                        ), StandardCharsets.UTF_8
                    )
                )
                reader.lines().forEachOrdered(Consumer { l: String? ->
                    allowedIngredients.addAll(
                        listOf(
                            listOf(
                                l?.split(",")?.toTypedArray()
                            ).toString()
                        )
                    )
                })
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}