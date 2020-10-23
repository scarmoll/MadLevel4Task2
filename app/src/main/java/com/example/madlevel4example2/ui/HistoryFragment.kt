package com.example.madlevel4example2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madlevel4example2.R
import com.example.madlevel4example2.model.Game
import com.example.madlevel4example2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private lateinit var gameListAdapter: GameListAdapter
    private var gameList: ArrayList<Game> = arrayListOf()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRepository = GameRepository(requireContext())

        initFragment()
    }

    private fun initFragment() {
        gameListAdapter = GameListAdapter(gameList)
        rvGames.adapter = gameListAdapter
        rvGames.layoutManager = LinearLayoutManager(activity)
        getListFromDatabase()

        rvGames.addItemDecoration(
            DividerItemDecoration(
                activity, DividerItemDecoration.VERTICAL
            )
        )


        //TODO: bind an itemTouchHelper to the recyclerview
    }


    private fun getListFromDatabase() {
        mainScope.launch {
            val games = withContext(Dispatchers.IO) { // retrieve stored games
                gameRepository.getAllGames()
            }

            this@HistoryFragment.gameList.clear() // clear local list
            this@HistoryFragment.gameList.addAll(games) // fill local list
            this@HistoryFragment.gameListAdapter.notifyDataSetChanged() // refresh list
        }
    }
}