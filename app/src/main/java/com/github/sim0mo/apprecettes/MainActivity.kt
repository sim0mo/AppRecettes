package com.github.sim0mo.apprecettes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.sim0mo.apprecettes.model.Recueil
import com.github.sim0mo.apprecettes.model.ingredients.Composant
import com.github.sim0mo.apprecettes.model.ingredients.Ingredient
import com.github.sim0mo.apprecettes.model.ingredients.Unite
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.streams.toList

class MainActivity : AppCompatActivity() {
    private val keyb: java.util.Scanner = java.util.Scanner(System.`in`)
    private lateinit var basicIngredients: List<Composant>
    private lateinit var catherine1 : Recueil
    private lateinit var gastronogeek : Recueil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()

        mainTest()

    }

    private fun initDB(){
        // TODO: use a synonym provider or sthg else
        Ingredient.init(this)

        basicIngredients = parseFrigo("frigo.txt")

        gastronogeek = load("gastronogeek.txt")
        catherine1 = load("catherine1.txt")
    }

    private fun mainTest(){
        log("")
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
        ).forEach{r -> log(r.toString())}
        log("-----")

        gastronogeek.searchConjunctive("GIN").forEach{x -> log(x.name)}

//        gastronogeek.printAllRecettesWith("vodka" );

        //gastronoGeek.recettesDisponibles(promptIngredientsIllimited())).forEach(System.out::println);
//        catherine1.printAllRecettesWith("LAIT");
//        System.out.println();

//        catherine1.recettesPresqueDisponibles(promptIngredientsIllimited()).forEach(System.out::println);
//        System.out.println();

//        catherine1.searchDisjunctive(List.of("SAFRAN","CANNELLE")).forEach(System.out::println);
//        System.out.println("***");
    }

    private fun log(msg : String) {
        Log.println(Log.ASSERT, "***", msg)
    }

    private fun load(s: String): Recueil {
        return Recueil.Loader().loadFrom(applicationContext.assets.open(s)).build()
    }

    private fun parseFrigo(fileName : String) : List<Composant> {
        val reader = BufferedReader(InputStreamReader(this.assets.open(fileName)))
        return reader.lines().map {
            Composant.parse("100kg $it")
        }.toList()
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
        val list: MutableList<Composant> = basicIngredients.toMutableList()
        var input: String = keyb.nextLine()
        while (!input.isEmpty()) {
            list.add(Composant.parse("1000kg $input"))
            input = keyb.nextLine()
        }
        return list
    }
}
