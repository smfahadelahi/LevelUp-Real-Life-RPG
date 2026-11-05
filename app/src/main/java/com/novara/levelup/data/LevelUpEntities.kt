package com.novara.levelup.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="player")
data class PlayerEntity(
 @PrimaryKey val id:Int=1,
 val totalXp:Int=0,
 val strengthXp:Int=0,
 val intelligenceXp:Int=0,
 val wellnessXp:Int=0,
 val disciplineXp:Int=0,
 val creativityXp:Int=0
)
@Entity(tableName="quests")
data class QuestEntity(
 @PrimaryKey val id:Int,
 val title:String,
 val detail:String,
 val difficulty:String,
 val category:String,
 val xpReward:Int,
 val lastCompletedDate:String?=null
)
data class QuestWithStatus(val id:Int,val title:String,val detail:String,val difficulty:String,val category:String,val xpReward:Int,val completedToday:Boolean)
@Entity(tableName="habits")
data class HabitEntity(@PrimaryKey val id:Int,val title:String)
@Entity(tableName="habit_checkins",primaryKeys=["habitId","date"])
data class HabitCheckInEntity(val habitId:Int,val date:String)
data class HabitWithStatus(val id:Int,val title:String,val completedToday:Boolean)
