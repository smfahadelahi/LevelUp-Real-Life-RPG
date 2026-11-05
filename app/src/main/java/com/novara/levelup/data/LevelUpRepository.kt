package com.novara.levelup.data
import kotlinx.coroutines.flow.Flow
class LevelUpRepository(private val dao:LevelUpDao) {
 val player:Flow<PlayerEntity?> = dao.observePlayer()
 val quests:Flow<List<QuestEntity>> = dao.observeQuests()
 val habits:Flow<List<HabitEntity>> = dao.observeHabits()
 suspend fun initialize() {
  dao.insertPlayer(PlayerEntity())
  dao.insertQuests(listOf(
   QuestEntity(1,"Morning movement","Move your body for 20 minutes","NORMAL",25),
   QuestEntity(2,"Read 10 pages","Invest in your intelligence","EASY",10),
   QuestEntity(3,"Deep work session","Focus for 45 minutes","HARD",50),
   QuestEntity(4,"Plan tomorrow","Write your top three priorities","EASY",10)
  ))
  dao.insertHabits(listOf(HabitEntity(1,"Drink enough water"),HabitEntity(2,"Read every day"),HabitEntity(3,"Move for 20 minutes"),HabitEntity(4,"Sleep on time")))
 }
 suspend fun claimQuest(id:Int)=dao.claimQuest(id)
 suspend fun setHabitCompleted(id:Int,completed:Boolean)=dao.setHabitCompleted(id,completed)
}
