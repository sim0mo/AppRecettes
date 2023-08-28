package com.github.sim0mo.apprecettes

import android.os.Bundle
import android.util.Log
import android.widget.*
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
    private lateinit var allowedIngredients : List<String>
    private lateinit var recueils: List<Recueil>

    private lateinit var recueilSpinner : Spinner
    private lateinit var ingredientSpinner : Spinner
    private lateinit var resultsTextView: TextView
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()

        recueilSpinner = findViewById(R.id.recueilSpinner)
        ingredientSpinner = findViewById(R.id.ingedientSpinner)
        resultsTextView = findViewById(R.id.resultsTextView)
        searchButton = findViewById(R.id.searchButton1)

        recueilSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            recueils.map { x -> x.name }
        )

        // TODO: Use a search bar instead of a spinner
        ingredientSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            allowedIngredients.sorted()
        )

//        mainTest()
        searchButton.setOnClickListener {
            val ingredient = ingredientSpinner.selectedItem.toString()
            val recueil = recueils[recueilSpinner.selectedItemPosition]
            val results = recueil.search(ingredient)
            resultsTextView.text = ""
            results.forEach{ x -> resultsTextView.text = resultsTextView.text.toString() + "\n" + x.name}
        }
    }

    /**
     * Initiates all Recueils and Frigo as well as synonyms
     */
    private fun initDB(){
        // TODO: use a synonym provider or sthg else
        Ingredient.init(this)
        allowedIngredients = Ingredient.getAllowedIngredients()

        basicIngredients = parseFrigo("frigo.txt")

        val gastronogeek = load("gastronogeek.txt", "Gastronogeek, le livre des potions")
        val catherine1 = load("catherine1.txt", "Catherine 1")
        recueils = listOf(gastronogeek, catherine1)
    }

    private fun mainTest(){
        log("")
//        catherine1.recettesPresqueDisponibles(
//            listOf(
//                Composant(Ingredient("OLIVE"), 300, Unite.G),
//                Composant(Ingredient("ANCHOIS"), 100, Unite.G),
//                Composant(Ingredient("THON"), 101, Unite.G),
//                Composant(Ingredient("AIL"), 5),
//                Composant(Ingredient("FARINE"), 300),
//                Composant(Ingredient("OEUF"), 10),
//                Composant(Ingredient("SUCRE"), 300, Unite.G),
//                Composant(Ingredient("BEURRE"), 500),
//                Composant(Ingredient("LAIT"), 5, Unite.DL),
//                Composant(Ingredient("CHOCOLAT_NOIR"), 600, Unite.G)
//            )
//        ).forEach{r -> log(r.toString())}
//        log("-----")

        recueils[0].search("VODKA").forEach{ x -> resultsTextView.text = resultsTextView.text.toString() + "\n" + x.name}

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

    private fun load(s: String, name: String): Recueil {
        return Recueil.Loader().loadFrom(applicationContext.assets.open(s)).build(name)
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
