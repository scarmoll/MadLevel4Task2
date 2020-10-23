package com.example.madlevel4example2.repository

import android.content.Context
import com.example.madlevel4example2.dao.GameDao
import com.example.madlevel4example2.database.GameListRoomDatabase
import com.example.madlevel4example2.enums.GameState
import com.example.madlevel4example2.model.Game

class GameRepository(context: Context) {
    private val gameDao: GameDao

    init {
        val database = GameListRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun getAllGames(): List<Game> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(product: Game) {
        gameDao.insertGame(product)
    }

    suspend fun deleteGame(product: Game) {
        gameDao.deleteGame(product)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

    suspend fun getAllWins(): Int {
        return gameDao.getGameStateTotal(GameState.WIN)
    }

    suspend fun getAllDraws(): Int {
        return gameDao.getGameStateTotal(GameState.DRAW)
    }

    suspend fun getAllLoses(): Int {
        return gameDao.getGameStateTotal(GameState.LOSE)
    }
}