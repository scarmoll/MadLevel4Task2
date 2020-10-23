package com.example.madlevel4example2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.madlevel4example2.R
import com.example.madlevel4example2.enums.GameState
import com.example.madlevel4example2.enums.Moves
import com.example.madlevel4example2.model.Game
import com.example.madlevel4example2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRepository = GameRepository(requireContext())
        getGamesStatsFromDatabase()
        initView()
    }

    /**
     * This method will set the listeners
     */
    private fun initView() {
        imv_rock.setOnClickListener { playGame(Moves.ROCK) }
        imv_paper.setOnClickListener { playGame(Moves.PAPER) }
        imv_scissors.setOnClickListener { playGame(Moves.SCISSORS) }
    }

    /**
     * This method will launch the game
     */
    private fun playGame(userMove: Moves) {
        when (userMove) {
            Moves.ROCK -> imv_move_player.setImageResource(R.drawable.rock)
            Moves.PAPER -> imv_move_player.setImageResource(R.drawable.paper)
            Moves.SCISSORS -> imv_move_player.setImageResource(R.drawable.scissors)
        }

        val movesByComputer = randomMoveComputer()

        when (checkForWin(movesByComputer, userMove)) {
            GameState.WIN -> {
                txt_game_result.text = getString(R.string.title_win)
            }
            GameState.DRAW -> {
                txt_game_result.text = getString(R.string.title_draw)
            }
            GameState.LOSE -> {
                txt_game_result.text = getString(R.string.title_lose)
            }
        }
    }

    /**
     * This method will do a random move for the computer and returns the move of the computer
     */
    private fun randomMoveComputer(): Moves {
        val randomNumber = (0 until 3).random()

        when (numberToMove(randomNumber)) {
            Moves.ROCK -> imv_move_computer.setImageResource(R.drawable.rock)
            Moves.PAPER -> imv_move_computer.setImageResource(R.drawable.paper)
            Moves.SCISSORS -> imv_move_computer.setImageResource(R.drawable.scissors)
        }

        return numberToMove(randomNumber)
    }

    /**
     * This method will transform a number into a Move
     */
    private fun numberToMove(number: Int): Moves {
        if (number == 0) {
            return Moves.ROCK
        } else if (number == 1) {
            return Moves.PAPER
        }

        return Moves.SCISSORS
    }

    /**
     * This method will check who has won the round
     */
    private fun checkForWin(computerMove: Moves, userMove: Moves): GameState {
        if (computerMove == userMove) {
            storeGame(
                Game(
                    state = GameState.DRAW.toString(),
                    date = Date().time,
                    computerMove = computerMove.toString(),
                    playerMove = userMove.toString()
                )
            )
            return GameState.DRAW
        } else if (computerMove == Moves.ROCK && userMove == Moves.PAPER ||
            computerMove == Moves.PAPER && userMove == Moves.SCISSORS ||
            computerMove == Moves.SCISSORS && userMove == Moves.ROCK
        ) {
            storeGame(
                Game(
                    state = GameState.WIN.toString(),
                    date = Date().time,
                    computerMove = computerMove.toString(),
                    playerMove = userMove.toString()
                )
            )
            return GameState.WIN
        } else if (userMove == Moves.ROCK && computerMove == Moves.PAPER ||
            userMove == Moves.PAPER && computerMove == Moves.SCISSORS ||
            userMove == Moves.SCISSORS && computerMove == Moves.ROCK
        ) {
            storeGame(
                Game(
                    state = GameState.LOSE.toString(),
                    date = Date().time,
                    computerMove = computerMove.toString(),
                    playerMove = userMove.toString()
                )
            )
            return GameState.LOSE
        }

        return GameState.LOSE;
    }

    /**
     * This method will retrieve all the games statistics from the database and update the ui
     */
    private fun getGamesStatsFromDatabase() {
        mainScope.launch {
            val numOfWins = withContext(Dispatchers.IO) { gameRepository.getAllWins() }
            val numOfDraws = withContext(Dispatchers.IO) { gameRepository.getAllDraws() }
            val numOfLoses = withContext(Dispatchers.IO) { gameRepository.getAllLoses() }

            txt_wins.text = getString(R.string.txt_win, numOfWins)
            txt_draws.text = getString(R.string.txt_draw, numOfDraws)
            txt_loses.text = getString(R.string.txt_loses, numOfLoses)
        }
    }

    /**
     * This method will store the played game to the database
     */
    private fun storeGame(currentGame: Game) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(currentGame)
            }

            getGamesStatsFromDatabase()
        }
    }
}