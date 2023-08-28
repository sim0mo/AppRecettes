package com.github.sim0mo.apprecettes.model.ingredients

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.github.sim0mo.apprecettes.R
import com.github.sim0mo.apprecettes.Utility
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Ingrédient : a un nom qui doit être contenu dans ingredients.txt
 * Peut avoir une spécification qui n'a pas d'influence sur l'égalité
 */
class Ingredient {
    val name: String
    var specification = ""
        private set

    constructor(nom: String) {
        name = nom
    }

    constructor(nom: String, specification: String) {
        name = nom
        this.specification = specification
    }

    fun fullName() : String {
        return if (specification == "") name else name + specification
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if ((other == null) || (javaClass != other.javaClass)) return false
        val that = other as Ingredient
        return fullName() == that.fullName()
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        //Ingredient in first position of each line will be considered as standard form
        private var initOK : Boolean = false
        private val synonyms: MutableMap<String, String> = java.util.HashMap<String, String>()
        private val allowedIngredients: MutableSet<String> = HashSet()

        /**
         * Given a raw string, outputs an Ingredient corresponding to it.
         * Checks that the ingredient exists, reduces it to its main synonym and cleans the string.
         */
        fun fromString(s: String): Ingredient {
            if(!initOK){
                throw Exception("Initialization of companion object is required.")
            }
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
                    "Aucun ingrédient correspondant à <<${result.name}>> n'est répertorié."
            }
            return result
        }

        /**
         * To be called only once with a valid context
         * Loads synonyms file and file of allowed ingredients
         */
        fun init(ctx : Context) {
            if (initOK) {
                return
            } else {
                try {
                    val ingredientsReader = BufferedReader(
                        InputStreamReader(
                            ctx.resources.openRawResource(R.raw.ingredients)
                            //applicationContext.assets.open("raw/ingredients.txt"), StandardCharsets.UTF_8
                        )
                    )
                    val synonymsReader = BufferedReader(
                        InputStreamReader(
                            ctx.resources.openRawResource(R.raw.synonyms)
                            //applicationContext.assets.open("raw/synonyms.txt"), StandardCharsets.UTF_8
                        )
                    )
                    ingredientsReader.lines().forEachOrdered { l: String? ->
                        allowedIngredients.addAll(l?.split(",")!!)
                    }
                    synonymsReader.lines().forEachOrdered { l: String? ->
                        val line: List<String> =l?.split(",")!!
                        for (synonym in line.subList(1, line.size)) {
                            synonyms[synonym] = line[0]
                        }
                    }

                    initOK = true
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }

        fun getAllowedIngredients(): List<String> {
            return allowedIngredients.toList()
        }
    }
}