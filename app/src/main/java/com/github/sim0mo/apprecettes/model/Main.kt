package com.github.sim0mo.apprecettes.model

import com.github.sim0mo.apprecettes.model.ingredients.Composant
import com.github.sim0mo.apprecettes.model.ingredients.Ingredient
import com.github.sim0mo.apprecettes.model.ingredients.Unite

object Main {
    private val keyb: java.util.Scanner = java.util.Scanner(System.`in`)
    private val basic: List<Composant> = parseFrigo("/frigo.txt")
    private fun parseFrigo(fileName: String): List<Composant> {

        return Main::class.java.getResource(fileName)!!.readText().lines().map { Composant.parse("1000kg \$s") }
    }

    @kotlin.jvm.JvmStatic
    fun main(args: Array<String>) {
        val gastronogeek: Recueil = load("/gastronogeek.txt")
        val catherine1: Recueil = load("/catherine1.txt")
        //        gastronogeek.printAllRecettesWith("vodka" );

        //gastronoGeek.recettesDisponibles(promptIngredientsIllimited())).forEach(System.out::println);
//        catherine1.printAllRecettesWith("LAIT");
//        System.out.println();

//        catherine1.recettesPresqueDisponibles(promptIngredientsIllimited()).forEach(System.out::println);
//        System.out.println();

//        catherine1.searchDisjunctive(List.of("SAFRAN","CANNELLE")).forEach(System.out::println);
//        System.out.println("***");
        catherine1.recettesPresqueDisponibles(
            listOf(
                Composant(Ingredient("OLIVE"), 300, Unite.G),
                Composant(Ingredient("ANCHOIS"), 100, Unite.G),
                Composant(Ingredient("THON"), 101, Unite.G),
                Composant(Ingredient("AIL"), 5),
                Composant(Ingredient("FARINE"), 300),
                Composant(Ingredient("OEUF"), 10),
                Composant(Ingredient("SUCRE"), 300, Unite.G),
                Composant(Ingredient("BEURRE"), 500),
                Composant(Ingredient("LAIT"), 5, Unite.DL),
                Composant(Ingredient("CHOCOLAT_NOIR"), 600, Unite.G)
            )
        ).forEach(java.lang.System.out::println)
        //gastronoGeek.recettesContenant(EAU)).forEach(x -> System.out.println(x.getName()));

        // TODO: 18.04.20 gui
    }

    private fun load(s: String): Recueil {
        return Recueil.Loader().loadFrom(Main::class.java.getResourceAsStream(s)).build()
    }

    private fun promptIngredientNames(): List<String> {
        val list: MutableList<String> = java.util.ArrayList<String>()
        var input: String = keyb.nextLine()
        while (input.isNotEmpty()) {
            list.add(Ingredient.fromString(input).name)
            input = keyb.nextLine()
        }
        return list
    }

    private fun promptIngredientsIllimited(): List<Composant> {
        val list: MutableList<Composant> = basic.toMutableList()
        var input: String = keyb.nextLine()
        while (!input.isEmpty()) {
            list.add(Composant.parse("1000kg $input"))
            input = keyb.nextLine()
        }
        return list
    }

    fun cleanString(s: String): String {
        var s = s
        s = s
            .replace("[èéê]".toRegex(), "e")
            .replace("[àâ]".toRegex(), "a")
            .replace(" \\(".toRegex(), "(")
            .replace(" ".toRegex(), "_")
            .replace("'".toRegex(), "_")
        s = s.uppercase()
        s = s.trim { it <= ' ' }
        return s
    }
}