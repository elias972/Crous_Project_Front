// ListFragment.kt
package com.example.crous_project

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast


class ListFragment : Fragment() {

    private val crousRepository = CrousRepository
    private lateinit var crousAdapter: CrousAdapter
    private lateinit var recyclerView: RecyclerView
    private var listener: OnCrousSelectedListener? = null
    private var showFavoritesOnly: Boolean = false

    //pagination related
    private var currentPage = 1
    private val pageSize = 5


    interface OnCrousSelectedListener {
        fun onCrousSelected(crous: Crous)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCrousSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCrousSelectedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showFavoritesOnly = it.getBoolean("showFavoritesOnly", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = rootView.findViewById(R.id.frag_list_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        // Pagination Buttons
        val btnNext: View = rootView.findViewById(R.id.btn_next_page)
        val btnPrevious: View = rootView.findViewById(R.id.btn_previous_page)

        btnNext.setOnClickListener { goToNextPage() }
        btnPrevious.setOnClickListener { goToPreviousPage() }

        loadCrousList()

        return rootView
    }


    private fun loadCrousList() {
        val crousList = if (showFavoritesOnly) {
            crousRepository.getAllCrous().filter { it.favorite }
        } else {
            crousRepository.getPaginatedCrous(currentPage, pageSize) // Apply pagination here
        }

        crousAdapter = CrousAdapter(
            crousList,
            onItemClick = { crous -> listener?.onCrousSelected(crous) },
            onFavoriteClick = { crous ->
                crous.favorite = !crous.favorite
                crousRepository.updateFavoriteStatus(crous.id, crous.favorite, requireContext())
                updateCrousList(
                    if (showFavoritesOnly) {
                        crousRepository.getAllCrous().filter { it.favorite }
                    } else {
                        crousRepository.getPaginatedCrous(currentPage, pageSize)
                    }
                )
            }
        )
        recyclerView.adapter = crousAdapter
    }



    fun updateCrousList(crousList: List<Crous>) {
        crousAdapter.updateCrousList(crousList)
    }

    // Pagination logic: go next
    fun goToNextPage() {
        val totalPages = (crousRepository.getAllCrous().size + pageSize - 1) / pageSize
        if (currentPage < totalPages) {
            currentPage++
            loadCrousList()
        } else {
            context?.let { Toast.makeText(it, "No more pages", Toast.LENGTH_SHORT).show() }
        }
    }
    // Pagination logic: go back
    fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            loadCrousList()
        } else {
            // Optional: Show a toast if the user tries to go before the first page
            context?.let { Toast.makeText(it, "Already on the first page", Toast.LENGTH_SHORT).show() }
        }
    }

}