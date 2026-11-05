package com.novara.levelup.data
import kotlinx.coroutines.flow.Flow
class LevelUpRepository(private val dao:LevelUpDao){
 val player:Flow<PlayerEntity?>=dao.observePlayer()
 fun quests(today:String):Flow<List<QuestWithStatus>>=dao.observeQuests(today)
 fun habits(today:String):Flow<List<HabitWithStatus>>=dao.observeHabits(today)
 suspend fun initialize(){
  dao.insertPlayer(PlayerEntity())
  dao.insertQuests(listOf(
   QuestEntity(1,"Morning movement","Move your body for 20 minutes","NORMAL","STRENGTH",25),
   QuestEntity(2,"Read 10 pages","Invest in your intelligence","EASY","INTELLIGENCE",10),
   QuestEntity(3,"Deep work session","Focus for 45 minutes","HARD","DISCIPLINE",50),
   QuestEntity(4,"Plan tomorrow","Write your top three priorities","EASY","WELLNESS",10)
  ))
  dao.insertHabits(listOf(HabitEntity(1,"Drink enough water"),HabitEntity(2,"Read every day"),HabitEntity(3,"Move for 20 minutes"),HabitEntity(4,"Sleep on time")))
 }
 suspend fun addQuest(title:String,detail:String,difficulty:String,category:String){
  val xp=when(difficulty){"EASY"->10;"NORMAL"->25;"HARD"->50;else->100}
  dao.createQuest(title,detail,difficulty,category,xp)
 }
 suspend fun addHabit(title:String){dao.createHabit(title)}
 suspend fun claimQuest(id:Int,today:String)=dao.claimQuest(id,today)
 suspend fun setHabitCompleted(id:Int,completed:Boolean,today:String)=dao.setHabitCompleted(id,completed,today)
}