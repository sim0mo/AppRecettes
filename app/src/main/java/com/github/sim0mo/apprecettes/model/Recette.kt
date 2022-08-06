package com.github.sim0mo.apprecettes.model

import com.github.sim0mo.apprecettes.model.ingredients.Composant
import kotlin.math.floor

/**
 * Une Recette contient des Composants et a un nom.
 */
class Recette(val name: String) {
    val ingredients: MutableList<Composant> = mutableListOf()
    get() {
        return java.util.Collections.unmodifiableList(field)
    }

    fun addComposant(composant: Composant) {
        for (c in ingredients) {
            if (java.util.Objects.equals(
                    c.ingredient.name,
                    composant.ingredient.name
                ) &&
                c.ingredient.specification == composant.ingredient.specification
            ) {
                throw java.lang.IllegalArgumentException(
                    java.lang.String.format(
                        "Déjà dans la recette %s ! (%s)",
                        name, composant.ingredient.name
                    )
                )
            }
        }
        ingredients.add(composant)
    }

    override fun toString(): String {
        val sb: java.lang.StringBuilder =
            java.lang.StringBuilder().append(name).append("\n---------------\n")
        for (e in ingredients) {
            if (e.quantite != 0.0) if (floor(e.quantite) == e.quantite) sb.append(
                e.quantite.toInt()
            ) else sb.append(e.quantite)
            if (e.unite != null) sb.append(
                java.lang.String.valueOf(e.unite).lowercase(java.util.Locale.getDefault())
            )
            sb.append(" ")
                .append(e.ingredient.name) //.append(e.ingredient.getSpecification())
                .append("\n")
        }
        return sb.toString()
    }

    operator fun contains(ingredientNom: String?): Boolean {
        for (c in ingredients) {
            if (c.ingredient.name == ingredientNom) {
                return true
            }
        }
        return false
    }



    val ingredientNames: List<String>
        get() {
            val list: MutableList<String> = java.util.ArrayList<String>()
            for (i in ingredients) {
                list.add(i.ingredient.name)
            }
            return list
        }
}