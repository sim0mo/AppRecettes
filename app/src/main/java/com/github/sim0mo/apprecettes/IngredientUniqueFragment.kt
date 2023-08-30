package com.github.sim0mo.apprecettes

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.github.sim0mo.apprecettes.model.Recueil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IngredientUniqueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IngredientUniqueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recueilSpinner : Spinner
    private lateinit var ingredientSpinner : Spinner
    private lateinit var resultsTextView: TextView
    private lateinit var searchButton: Button

    private lateinit var recueils: List<Recueil>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recueils = (activity as MainActivity).recueils
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredient_unique, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        recueilSpinner = requireView().findViewById(R.id.recueilSpinner)
        ingredientSpinner = requireView().findViewById(R.id.ingedientSpinner)
        resultsTextView = requireView().findViewById(R.id.resultsTextView)
        resultsTextView.movementMethod = ScrollingMovementMethod()
        searchButton = requireView().findViewById(R.id.searchButton1)

        recueilSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            // TODO: Put global in a separate file
            recueils.map { x -> x.name }
        )

        // TODO: Use a search bar instead of a spinner
        ingredientSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            (activity as MainActivity).allowedIngredients.sorted()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IngredientUniqueFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IngredientUniqueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}