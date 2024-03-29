package com.github.sim0mo.apprecettes.model.ingredients

/**
 * Un Composant est un Ingrédient avec une quantité et une unité
 */
class Composant(val ingredient: Ingredient, val quantite: Double, val unite: Unite?) {

    constructor(ingredient: Ingredient, quantite: Int, unite: Unite?) : this(ingredient, quantite.toDouble(), unite)
    constructor(ingredient: Ingredient, quantite: Int) : this(ingredient, quantite.toDouble(), null)
    constructor(ingredient: Ingredient, quantite: Double) : this(ingredient, quantite, null)
    constructor(ingredient: Ingredient) : this(ingredient, 1.0, null)

    /**
     * La quantité à comparer en unité fondamentale (g/l/cs/cc)
     */
    fun quantiteFondamentale(): Double {
        return if (unite == null) quantite else quantite * unite.coeff
    }



    companion object {

        fun parse(s: String): Composant {
            val s1 = s.trim()
            if(!s1[0].isDigit()){
                return Composant(Ingredient.fromString(s1))
            }
            val first: String = s1.substringBefore(" ")
            val second: String = s1.substringAfter(" ")

            //System.out.println(zero);
            val `in`: Ingredient = Ingredient.fromString(second)
            val un = Unite.parse(first)
            val qu = Unite.parseNumber(first)
            return Composant(`in`, qu, un)
        }
    }
}