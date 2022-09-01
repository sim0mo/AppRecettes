package com.github.sim0mo.apprecettes.model.ingredients

import android.app.Application
import com.github.sim0mo.apprecettes.Utility
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

    companion object : Application() {
        //Ingredient in first position of each line will be considered as standard form
        private val synonyms: MutableMap<String, String> = java.util.HashMap<String, String>()
        private val allowedIngredients: MutableSet<String> = HashSet()
        fun fromString(s: String): Ingredient {
            var s1 = s
            s1 = Utility.cleanString(s1)
            val result: Ingredient = if (s1.contains("(")) {
                Ingredient(
                    s1.substring(0, s1.indexOf("(")),
                    s1.substring(s1.indexOf("("), s1.indexOf(")") + 1)
                )
            } else {
                Ingredient(s1)
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
                val ingredientsReader = BufferedReader(
                    InputStreamReader(
                        applicationContext.assets.open("ingredients.txt"), StandardCharsets.UTF_8
                    )
                )
                val synonymsReader = BufferedReader(
                    InputStreamReader(
                        applicationContext.assets.open("synonyms.txt"), StandardCharsets.UTF_8
                    )
                )
                ingredientsReader.lines().forEachOrdered { l: String? ->
                    allowedIngredients.addAll(
                        listOf(
                            listOf(l?.split(",")?.toTypedArray()).toString()
                        )
                    )
                }
                synonymsReader.lines().forEachOrdered { l: String? ->
                    val line: List<String> =
                        listOf(l?.split(",")?.toTypedArray().toString()
                        )
                    for (synonym in line.subList(1, line.size)) {
                        synonyms[synonym] = line[0]
                    }
                }

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}