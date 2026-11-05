package com.novara.levelup.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LevelUpDao {
    @Query("SELECT * FROM player WHERE id = 1")
    abstract fun observePlayer(): Flow<PlayerEntity?>

    @Query("""
        SELECT id, title, detail, difficulty, xpReward,
        CASE WHEN lastCompletedDate = :today THEN 1 ELSE 0 END AS completedToday
        FROM quests ORDER BY id
    """)
    abstract fun observeQuests(today: String): Flow<List<QuestWithStatus>>

    @Query("""
        SELECT habits.id, habits.title,
        CASE WHEN habit_checkins.habitId IS NULL THEN 0 ELSE 1 END AS completedToday
        FROM habits
        LEFT JOIN habit_checkins ON habits.id = habit_checkins.habitId AND habit_checkins.date = :today
        ORDER BY habits.id
    """)
    abstract fun observeHabits(today: String): Flow<List<HabitWithStatus>>

    @Query("SELECT * FROM quests WHERE id = :id")
    abstract suspend fun getQuest(id: Int): QuestEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPlayer(player: PlayerEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertQuests(quests: List<QuestEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertHabits(habits: List<HabitEntity>)

    @Query("UPDATE quests SET lastCompletedDate = :today WHERE id = :id AND (lastCompletedDate IS NULL OR lastCompletedDate != :today)")
    abstract suspend fun markQuestComplete(id: Int, today: String): Int

    @Query("UPDATE player SET totalXp = totalXp + :amount WHERE id = 1")
    abstract suspend fun addXp(amount: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addHabitCheckIn(checkIn: HabitCheckInEntity)

    @Query("DELETE FROM habit_checkins WHERE habitId = :habitId AND date = :today")
    abstract suspend fun removeHabitCheckIn(habitId: Int, today: String)

    @Transaction
    open suspend fun claimQuest(id: Int, today: String): Boolean {
        val quest = getQuest(id) ?: return false
        if (quest.lastCompletedDate == today) return false
        if (markQuestComplete(id, today) != 1) return false
        addXp(quest.xpReward)
        return true
    }

    @Transaction
    open suspend fun setHabitCompleted(habitId: Int, completed: Boolean, today: String) {
        if (completed) addHabitCheckIn(HabitCheckInEntity(habitId, today))
        else removeHabitCheckIn(habitId, today)
    }
}
