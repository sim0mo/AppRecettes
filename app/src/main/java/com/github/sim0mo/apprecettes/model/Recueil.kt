package com.github.sim0mo.apprecettes.model

import com.github.sim0mo.apprecettes.model.ingredients.Composant
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors

/**
 * Un Recueil de Recettes.
 */
class Recueil private constructor(recettes: List<Recette>) {
    private val recettes: List<Recette>
    override fun toString(): String {
        val allRecettes = StringBuilder()
        for (r in recettes) allRecettes.append(r).append("\n")
        return allRecettes.toString()
    }

    fun searchConjunctive(String: String?): List<Recette> {
        val recettesFaisables: MutableList<Recette> = ArrayList()
        for (r in recettes) {
            if (r.contains(String)) recettesFaisables.add(r)
        }
        return recettesFaisables
    }

    fun searchConjunctive(ingredients: List<String?>): List<Recette> {
        val recettesFaisables: MutableList<Recette> = ArrayList()
        for (r in recettes) {
            if (r.contains(ingredients[0])) {
                recettesFaisables.add(r)
            }
        }
        for (i in ingredients.subList(1, ingredients.size)) {
            recettesFaisables.removeIf { r: Recette ->
                !r.contains(
                    i
                )
            }
        }
        return recettesFaisables
    }

    fun searchDisjunctive(ingredients: List<String?>): List<Recette> {
        val recettesFaisables: MutableList<Recette> = ArrayList()
        for (i in ingredients) {
            for (r in recettes) {
                if (r.contains(i) && !recettesFaisables.contains(r)) {
                    recettesFaisables.add(r)
                }
            }
        }
        return recettesFaisables
    }

    fun recettesDisponibles(ingredientsDisponibles: List<Composant>): List<Recette> {
        val recettesFaisables: MutableList<Recette> = ArrayList()
        for (r in recettes) {
            if ( //détermine si tous les ingrédients de la recette r sont disponibles
                r.ingredientNames.stream()
                    .allMatch { ingredientInRecipe: String ->
                        ingredientsDisponibles.stream()
                            .map { composant: Composant ->
                                composant.ingredient.name
                            }
                            .anyMatch { anObject: String? ->
                                ingredientInRecipe == anObject
                            }
                    }
            ) { //détermine si leur nombre est suffisant
                var faisable = true
                for (required in r.ingredients) {
                    for (disposable in ingredientsDisponibles) {
                        if (required.ingredient.name == disposable.ingredient.name) {
                            //System.out.println(required.quantiteFondamentale() + "   " + disposable.quantiteFondamentale());
                            faisable =
                                faisable and (required.quantiteFondamentale() <= disposable.quantiteFondamentale())
                        }
                    }
                }
                if (faisable) recettesFaisables.add(r)
            }
        }
        return recettesFaisables
    }

    @JvmOverloads
    fun recettesPresqueDisponibles(
        ingredientsDisponibles: List<Composant>,
        pourcentage: Double = 0.75
    ): List<Recette> {
        val recettesFaisables: MutableList<Recette> = ArrayList()
        for (r in recettes) {
            val nIngredients = r.ingredients.size
            var nMatches = 0
            for (c in r.ingredients) {
                for (d in ingredientsDisponibles) {
                    if (c.ingredient.name == d.ingredient.name &&
                        c.quantiteFondamentale() <= d.quantiteFondamentale()
                    ) {
                        nMatches++
                    }
                }
            }
            if (nMatches * 1.0 / nIngredients >= pourcentage) {
                recettesFaisables.add(r)
            }
        }
        return recettesFaisables
    }

    class Loader {
        private val recettes: MutableList<Recette> = ArrayList()
        fun loadFrom(inputStream: InputStream?): Loader {
            try {
                BufferedReader(
                    InputStreamReader( // TODO: 18.08.20 change charset
                        inputStream, StandardCharsets.UTF_8
                    )
                ).use { reader ->
                    reader.lines()
                        .map { s: String ->
                            s.split(
                                ", *"
                            ).toTypedArray()
                        }
                        .map { strings: Array<String> ->
                            val recette = Recette(strings[0])
                            for (i in 1 until strings.size) {
                                recette.addComposant(Composant.parse(strings[i]))
                            }
                            recette
                        }
                        .forEachOrdered { e: Recette ->
                            recettes.add(
                                e
                            )
                        }
                    return this
                }
            } catch (e: IOException) {
                throw UncheckedIOException(e)
            }
        }

        fun build(): Recueil {
            return Recueil(recettes)
        }
    }

    fun printAllRecettesWith(i1: String, vararg i: String) {
        System.out.printf("Recettes contenant : %s %s\n", i1, Arrays.toString(i))
        val l = Arrays.stream(i).map { s: String ->
            Main.cleanString(
                s
            )
        }.collect(Collectors.toList())
        l.add(0, Main.cleanString(i1))
        for (r in searchConjunctive(l)) {
            println(r.name)
        }
    }

    init {
        this.recettes = java.util.List.copyOf(recettes)
    }
}