package com.github.sim0mo.apprecettes.model

import com.github.sim0mo.apprecettes.model.ingredients.Composant
import kotlin.math.floor

/**
 * Une Recette contient des Composants et a un nom.
 */
class Recette(val name: String) {
    private val _composants : MutableList<Composant> = ArrayList()
    val composants : List<Composant>
        get() = _composants.toList()

    fun addComposant(composant: Composant) {
        for (c in composants) {
            if (c.ingredient.fullName() == composant.ingredient.fullName() &&
                c.ingredient.specification == composant.ingredient.specification) {
                throw IllegalArgumentException("Déjà dans la recette $name ! (${composant.ingredient.fullName()})")
            }
        }
        _composants.add(composant)
    }

    override fun toString(): String {
        val sb: StringBuilder =
            StringBuilder().append(name).append("\n---------------\n")
        for (e in composants) {
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
        for (c in composants) {
            if (c.ingredient.name == ingredientNom) {
                return true
            }
        }
        return false
    }



    val ingredientNames: List<String>
        get() {
            val list: MutableList<String> = java.util.ArrayList<String>()
            for (i in composants) {
                list.add(i.ingredient.fullName())
            }
            return list
        }
}