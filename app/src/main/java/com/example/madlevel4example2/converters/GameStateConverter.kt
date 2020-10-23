package com.example.madlevel4example2.converters

import androidx.room.TypeConverter
import com.example.madlevel4example2.enums.GameState


class GameStateConverter {
    @TypeConverter
    fun fromGameStateToString(state: GameState?): String? {
        when (state) {
            GameState.DRAW -> return GameState.DRAW.toString()
            GameState.LOSE -> return GameState.LOSE.toString()
        }

        return GameState.WIN.toString()
    }

    @TypeConverter
    fun stringToGameState(stringValue: String?): GameState? {
        when (stringValue) {
            GameState.DRAW.toString() -> return GameState.DRAW
            GameState.LOSE.toString() -> return GameState.LOSE
        }

        return GameState.WIN
    }
}