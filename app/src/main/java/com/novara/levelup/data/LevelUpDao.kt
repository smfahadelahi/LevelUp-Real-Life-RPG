package com.novara.levelup.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao abstract class LevelUpDao {
 @Query("SELECT * FROM player WHERE id=1") abstract fun observePlayer():Flow<PlayerEntity?>
 @Query("""SELECT id,title,detail,difficulty,category,xpReward,CASE WHEN lastCompletedDate=:today THEN 1 ELSE 0 END AS completedToday FROM quests ORDER BY id""") abstract fun observeQuests(today:String):Flow<List<QuestWithStatus>>
 @Query("""SELECT habits.id,habits.title,CASE WHEN habit_checkins.habitId IS NULL THEN 0 ELSE 1 END AS completedToday FROM habits LEFT JOIN habit_checkins ON habits.id=habit_checkins.habitId AND habit_checkins.date=:today ORDER BY habits.id""") abstract fun observeHabits(today:String):Flow<List<HabitWithStatus>>
 @Query("SELECT * FROM quests WHERE id=:id") abstract suspend fun getQuest(id:Int):QuestEntity?
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertPlayer(player:PlayerEntity)
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertQuests(quests:List<QuestEntity>)
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertHabits(habits:List<HabitEntity>)
 @Query("SELECT COALESCE(MAX(id),0)+1 FROM quests") abstract suspend fun nextQuestId():Int
 @Query("SELECT COALESCE(MAX(id),0)+1 FROM habits") abstract suspend fun nextHabitId():Int
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertQuest(quest:QuestEntity)
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertHabit(habit:HabitEntity)
 @Transaction open suspend fun createQuest(title:String,detail:String,difficulty:String,category:String,xp:Int){insertQuest(QuestEntity(nextQuestId(),title,detail,difficulty,category,xp))}
 @Transaction open suspend fun createHabit(title:String){insertHabit(HabitEntity(nextHabitId(),title))}
 @Query("UPDATE quests SET lastCompletedDate=:today WHERE id=:id AND (lastCompletedDate IS NULL OR lastCompletedDate!=:today)") abstract suspend fun markQuestComplete(id:Int,today:String):Int
 @Query("""UPDATE player SET totalXp=totalXp+:amount,strengthXp=strengthXp+CASE WHEN :category='STRENGTH' THEN :amount ELSE 0 END,intelligenceXp=intelligenceXp+CASE WHEN :category='INTELLIGENCE' THEN :amount ELSE 0 END,wellnessXp=wellnessXp+CASE WHEN :category='WELLNESS' THEN :amount ELSE 0 END,disciplineXp=disciplineXp+CASE WHEN :category='DISCIPLINE' THEN :amount ELSE 0 END,creativityXp=creativityXp+CASE WHEN :category='CREATIVITY' THEN :amount ELSE 0 END WHERE id=1""") abstract suspend fun addXp(amount:Int,category:String)
 @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun addHabitCheckIn(checkIn:HabitCheckInEntity)
 @Query("DELETE FROM habit_checkins WHERE habitId=:habitId AND date=:today") abstract suspend fun removeHabitCheckIn(habitId:Int,today:String)
 @Transaction open suspend fun claimQuest(id:Int,today:String):Boolean {
  val quest=getQuest(id) ?: return false
  if(quest.lastCompletedDate==today) return false
  if(markQuestComplete(id,today)!=1) return false
  addXp(quest.xpReward,quest.category)
  return true
 }
 @Transaction open suspend fun setHabitCompleted(habitId:Int,completed:Boolean,today:String) {
  if(completed) addHabitCheckIn(HabitCheckInEntity(habitId,today)) else removeHabitCheckIn(habitId,today)
 }
}