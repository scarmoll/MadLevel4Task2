package com.example.madlevel4example2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel4example2.enums.GameState
import com.example.madlevel4example2.model.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM gameTable")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(*) FROM gameTable WHERE state LIKE :gameState")
    suspend fun getGameStateTotal(gameState: GameState): Int
}