package com.novara.levelup.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao abstract class LevelUpDao {
 @Query("SELECT * FROM player WHERE id=1") abstract fun observePlayer():Flow<PlayerEntity?>
 @Query("SELECT * FROM quests ORDER BY id") abstract fun observeQuests():Flow<List<QuestEntity>>
 @Query("SELECT * FROM habits ORDER BY id") abstract fun observeHabits():Flow<List<HabitEntity>>
 @Query("SELECT * FROM quests WHERE id=:id") abstract suspend fun getQuest(id:Int):QuestEntity?
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertPlayer(player:PlayerEntity)
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertQuests(quests:List<QuestEntity>)
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertHabits(habits:List<HabitEntity>)
 @Query("UPDATE quests SET completed=1 WHERE id=:id AND completed=0") abstract suspend fun markQuestComplete(id:Int):Int
 @Query("UPDATE player SET totalXp=totalXp+:amount WHERE id=1") abstract suspend fun addXp(amount:Int)
 @Query("UPDATE habits SET completedToday=:completed WHERE id=:id") abstract suspend fun setHabitCompleted(id:Int,completed:Boolean)
 @Transaction open suspend fun claimQuest(id:Int):Boolean {
  val quest=getQuest(id) ?: return false
  if(quest.completed) return false
  if(markQuestComplete(id)!=1) return false
  addXp(quest.xpReward)
  return true
 }
}
