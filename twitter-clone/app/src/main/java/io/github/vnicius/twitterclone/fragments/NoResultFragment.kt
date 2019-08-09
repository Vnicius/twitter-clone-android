package io.github.vnicius.twitterclone.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.vnicius.twitterclone.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_QUERY = "query"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NoResultFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NoResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NoResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_result, container, false)
        view.findViewById<TextView>(R.id.tv_no_result).text = "No results for \"$query\""

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NoResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(query: String) =
            NoResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }
}
